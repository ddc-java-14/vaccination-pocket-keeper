package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

/**
 * Declares CRUD operations for the {@link Doctor} entity.
 */
@Dao
public interface DoctorDao {

  /**
   * Declares insert operation for a {@link Doctor} object.
   * @param doctor an object of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Long}.
   */
  @Insert
  Single<Long> insert(Doctor doctor);

  /**
   * Declares insert operation for an array of {@link Doctor} objects.
   * @param doctors an array of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Doctor... doctors);

  /**
   * Declares insert operation for a {@link Collection} of {@link Doctor} objects.
   * @param doctors a {@link Collection} of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Collection<Doctor> doctors);

  /**
   * Declares update operation for a {@link Doctor} object.
   * @param doctor an object of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Doctor doctor);//return type is number of records affected by the operation

  /**
   * Declares update operation for an array of {@link Doctor} objects.
   * @param doctors an array of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Doctor... doctors);

  /**
   * Declares update operation for a {@link Collection} of {@link Doctor} objects.
   * @param doctors a {@link Collection} of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Collection<Doctor> doctors);

  /**
   * Declares delete operation for a {@link Doctor} object.
   * @param doctor an object of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Doctor doctor);

  /**
   * Declares delete operation for an array of {@link Doctor} objects.
   * @param doctors an array of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Doctor... doctors);

  /**
   * Declares delete operation for a {@link Collection} of {@link Doctor} objects.
   * @param doctors a {@link Collection} of objects of type {@link Doctor}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Collection<Doctor> doctors);

  /**
   * Declares select operation for all {@link Doctor} objects sorted by date created in ascending order.
   * @return a reactivex {@link LiveData} {@link List} of {@link Doctor} objects.
   */
  @Query("SELECT * FROM doctor ORDER BY created ASC") //we named our table doctor
  LiveData<List<Doctor>> selectAll();

  /**
   * Declares select operation for a {@link Doctor} object where the doctor_id equals the doctor_id passed in from the caller.
   * @param doctorId a long primitive that uniquely identifies the {@link Doctor} object.
   * @return  a reactivex {@link LiveData} object of type {@link Doctor} object.
   */
  @Query("SELECT * FROM doctor WHERE doctor_id = :doctorId")
  LiveData<Doctor> select(long doctorId );
}
