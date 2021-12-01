package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.UserWithVaccines;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import io.reactivex.Single;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Declares CRUD operations for the {@link User} entity.
 */
@Dao
public interface UserDao {

  /**
   * Declares insert operation for a {@link User} object.
   * @param user an object of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Long}.
   */
  @Insert
  Single<Long> insert(User user);

  /**
   * Declares insert operation for an array of {@link User} objects.
   * @param users an array of objects of type {@link User}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(User... users);

  /**
   * Declares insert operation for a {@link Collection} of {@link User} objects.
   * @param users a {@link Collection} of objects of type {@link User}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Collection<User> users);

  /**
   * Declares update operation for a {@link User} object.
   * @param user an object of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(User user);//return type is number of records affected by the operation

  /**
   * Declares update operation for an array of {@link User} objects.
   * @param users an array of objects of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(User... users);

  /**
   * Declares update operation for a {@link Collection} of {@link User} objects.
   * @param users a {@link Collection} of objects of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Collection<User> users);

  /**
   * Declares delete operation for a {@link User} object.
   * @param user an object of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(User user);

  /**
   * Declares delete operation for an array of {@link User} objects.
   * @param users an array of objects of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(User... users);

  /**
   * Declares delete operation for a {@link Collection} of {@link User} objects.
   * @param users a {@link Collection} of objects of type {@link User}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Collection<User> users);

  /**
   * Declares select operation for all {@link User} objects sorted by date created in ascending order.
   * @return a reactivex {@link LiveData} {@link List} of {@link User} objects.
   */
  @Query("SELECT * FROM user ORDER BY created ASC") //we named our table dose
  LiveData<List<User>> selectAll();

  /**
   * Declares select operation for a {@link User} object where the user_id equals the user_id passed in from the caller.
   * @param userId a long primitive that uniquely identifies the {@link User} object.
   * @return  a reactivex {@link LiveData} object of {@link UserWithVaccines} objects.
   */
  @Transaction
  @Query("SELECT * FROM user WHERE user_id = :userId")
  LiveData<UserWithVaccines> select(long userId);
}
