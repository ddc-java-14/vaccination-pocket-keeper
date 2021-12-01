package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import io.reactivex.Single;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Declares CRUD operations for the {@link Vaccine} entity.
 */
@Dao
public interface VaccineDao {

   /**
   * Declares insert operation for a {@link Vaccine} object.
   * @param vaccine an object of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Long}.
   */
  @Insert
  Single<Long> insert(Vaccine vaccine);

  /**
   * Declares insert operation for an array of {@link Vaccine} objects.
   * @param vaccines an array of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Vaccine... vaccines);

  /**
   * Declares insert operation for a {@link Collection} of {@link Vaccine} objects.
   * @param vaccines a {@link Collection} of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Collection<Vaccine> vaccines);

  /**
   * Declares update operation for a {@link Vaccine} object.
   * @param vaccine an object of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Vaccine vaccine);//return type is number of records affected by the operation

  /**
   * Declares update operation for an array of {@link Vaccine} objects.
   * @param vaccines an array of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Vaccine... vaccines);

  /**
   * Declares update operation for a {@link Collection} of {@link Vaccine} objects.
   * @param vaccines a {@link Collection} of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Collection<Vaccine> vaccines);

  /**
   * Declares delete operation for a {@link Vaccine} object.
   * @param vaccine an object of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Vaccine vaccine);

  /**
   * Declares delete operation for an array of {@link Vaccine} objects.
   * @param vaccines an array of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Vaccine... vaccines);

  /**
   * Declares delete operation for a {@link Collection} of {@link Vaccine} objects.
   * @param vaccines a {@link Collection} of objects of type {@link Vaccine}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Collection<Vaccine> vaccines);

  /**
   * Declares select operation for all {@link Vaccine} objects sorted by date created in ascending order.
   * @return a reactivex {@link LiveData} {@link List} of {@link Vaccine} objects.
   */
  @Query("SELECT * FROM vaccine ORDER BY created ASC") //we named our table vaccine
  LiveData<List<Vaccine>> selectAll();

  /**
   * Declares select operation for a {@link Vaccine} object where the vaccine_id equals the vaccine_id passed in from the caller.
   * @param vaccineId a long primitive that uniquely identifies the {@link Vaccine} object.
   * @return  a reactivex {@link LiveData} object of {@link VaccineWithDoses} objects.
   */
  @Transaction
  @Query("SELECT * FROM vaccine WHERE vaccine_id = :vaccineId")
  LiveData<VaccineWithDoses> select(long vaccineId);

  /**
   * Declares select operation for a {@link VaccineSummary} object where date_administered
   * is less than or equal to the fromDate sorted by date_administered in ascending order.
   * @param fromDate a {@link Date} before which we select {@link Vaccine} objects.
   * @return a reactivex {@link LiveData} {@link List} of {@link VaccineSummary} objects.
   */
  @RewriteQueriesToDropUnusedColumns
  @Transaction
  @Query("SELECT * FROM vaccine_summary WHERE date_administered <= :fromDate ORDER BY date_administered ASC")
  LiveData<List<VaccineSummary>> selectPastVaccines(Date fromDate);

  /**
   * Declares select operation for a {@link VaccineSummary} object where date_administered
   * is greater than or equal to the fromDate sorted by date_administered in ascending order.
   * @param fromDate a {@link Date} after which we select {@link Vaccine} objects.
   * @return a reactivex {@link LiveData} {@link List} of {@link VaccineSummary} objects.
   */
  @RewriteQueriesToDropUnusedColumns
  @Transaction
  @Query("SELECT * FROM vaccine_summary WHERE date_administered >= :fromDate ORDER BY date_administered ASC")
  LiveData<List<VaccineSummary>> selectUpcomingVaccines(Date fromDate);

}
