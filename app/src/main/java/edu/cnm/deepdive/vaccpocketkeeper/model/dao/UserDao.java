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
import io.reactivex.Single;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  Single<Long> insert(User dose);

  @Insert
  Single<List<Long>> insert(User... users);

  @Insert
  Single<List<Long>> insert(Collection<User> users);

  @Update
  Single<Integer> update(User dose);//return type is number of records affected by the operation

  @Update
  Single<Integer> update(User... users);

  @Update
  Single<Integer> update(Collection<User> users);

  @Delete
  Single<Integer> delete(User dose);

  @Delete
  Single<Integer> delete(User... users);

  @Delete
  Single<Integer> delete(Collection<User> users);

  @Query("SELECT * FROM user ORDER BY created DESC") //we named our table dose
  LiveData<List<User>> selectAll();

  @Transaction
  @Query("SELECT * FROM user WHERE user_id = :userId")
  LiveData<UserWithVaccines> select(long userId);
}
