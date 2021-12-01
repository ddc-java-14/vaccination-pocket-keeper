package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import io.reactivex.Single;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Declares CRUD operations for the {@link Dose} entity.
 */
@Dao
public interface DoseDao {

  /**
   * Declares insert operation for a {@link Dose} object.
   * @param dose an object of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Long}.
   */
  @Insert
  Single<Long> insert(Dose dose);

  /**
   * Declares insert operation for an array of {@link Dose} objects.
   * @param doses an array of objects of type {@link Dose}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Dose... doses);

  /**
   * Declares insert operation for a {@link Collection} of {@link Dose} objects.
   * @param doses a {@link Collection} of objects of type {@link Dose}.
   * @return a reactivex {@link Single} {@link List} of type {@link Long}.
   */
  @Insert
  Single<List<Long>> insert(Collection<? extends Dose> doses);

  /**
   * Declares update operation for a {@link Dose} object.
   * @param dose an object of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Dose dose);//return type is number of records affected by the operation

  /**
   * Declares update operation for an array of {@link Dose} objects.
   * @param doses an array of objects of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Dose... doses);

  /**
   * Declares update operation for a {@link Collection} of {@link Dose} objects.
   * @param doses a {@link Collection} of objects of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Update
  Single<Integer> update(Collection<? extends Dose> doses);

  /**
   * Declares delete operation for a {@link Dose} object.
   * @param dose an object of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Dose dose);

  /**
   * Declares delete operation for an array of {@link Dose} objects.
   * @param doses an array of objects of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Dose... doses);

  /**
   * Declares delete operation for a {@link Collection} of {@link Dose} objects.
   * @param doses a {@link Collection} of objects of type {@link Dose}.
   * @return a reactivex {@link Single} of type {@link Integer}.
   */
  @Delete
  Single<Integer> delete(Collection<? extends Dose> doses);

  /**
   * Declares select operation for all {@link Dose} objects sorted by date created in ascending order.
   * @return a reactivex {@link LiveData} {@link List} of {@link DoseWithDoctor} objects.
   */
  @Transaction
  @Query("SELECT * FROM dose ORDER BY created ASC") //we named our table dose
  LiveData<List<DoseWithDoctor>> selectAll();

  /**
   * Declares select operation for a {@link Dose} object where the dose_id equals the dose_id passed in from the caller.
   * @param doseId a long primitive that uniquely identifies the {@link Dose} object.
   * @return  a reactivex {@link LiveData} object of {@link Dose} objects.
   */
  @Query("SELECT * FROM dose WHERE dose_id = :doseId")
  LiveData<Dose> select(long doseId);

  /**
   * Declares select operation for {@link Dose} objects where date_administered
   * is less than or equal to the fromDate sorted by date_administered in ascending order.
   * @param fromDate a {@link Date} before which we select {@link Dose} objects.
   * @return a reactivex {@link LiveData} {@link List} of {@link Dose} objects.
   */
  @Query("SELECT * FROM dose WHERE date_administered <= :fromDate ORDER BY date_administered ASC")
  LiveData<List<Dose>> selectPastDoses(Date fromDate);

  /**
   * Declares select operation for {@link Dose} objects where date_administered
   * is greater than or equal to the fromDate sorted by date_administered in ascending order.
   * @param fromDate a {@link Date} after which we select {@link Dose} objects.
   * @return a reactivex {@link LiveData} {@link List} of {@link Dose} objects.
   */
  @Query("SELECT * FROM dose WHERE date_administered >= :fromDate ORDER BY date_administered ASC") //we named our table dose
  LiveData<List<Dose>> selectAllFutureDoses(Date fromDate);

  /**
   * Declares select operation for {@link Dose} objects where vaccine_id
   * is equal to the vaccine_id passed in by the caller sorted by date_administered in ascending order.
   * @param vaccineId a long primitive that identifies the {@link Dose} object to which it belongs.
   * @return a reactivex {@link LiveData} {@link List} of {@link Dose} objects.
   */
  @Query("SELECT * FROM dose WHERE vaccine_id = :vaccineId ORDER BY date_administered ASC")
  LiveData<List<Dose>> selectAllDosesForVaccineId(long vaccineId);

  /**
   * Declares select operation for {@link Dose} objects where date_administered
   * is greater than or equal to the startDate and date_administered is less than or equal to endDate
   * sorted by date_administered in ascending order.
   * @param startDate a {@link Date} after which we select {@link Dose} objects.
   * @param endDate a {@link Date} before which we select {@link Dose} objects.
   * @return a reactivex {@link LiveData} {@link List} of {@link DoseWithDoctor} objects.
   */
  @Transaction
  @Query("SELECT * FROM dose WHERE date_administered >= :startDate and date_administered <= :endDate ORDER BY date_administered ASC")
  LiveData<List<DoseWithDoctor>> selectUpcomingDoses(Date startDate, Date endDate);
}
