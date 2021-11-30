package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface DoseDao {

  @Insert
  Single<Long> insert(Dose dose);

  @Insert
  Single<List<Long>> insert(Dose... doses);

  @Insert
  Single<List<Long>> insert(Collection<? extends Dose> doses);

  @Update
  Single<Integer> update(Dose dose);//return type is number of records affected by the operation

  @Update
  Single<Integer> update(Dose... doses);

  @Update
  Single<Integer> update(Collection<? extends Dose> doses);

  @Delete
  Single<Integer> delete(Dose dose);

  @Delete
  Single<Integer> delete(Dose... doses);

  @Delete
  Single<Integer> delete(Collection<? extends Dose> doses);

  @Query("SELECT * FROM dose ORDER BY created DESC") //we named our table dose
  LiveData<List<DoseWithDoctor>> selectAll();

  @Query("SELECT * FROM dose ORDER BY date_administered ASC") //we named our table dose
  LiveData<List<Dose>> selectAllFutureDoses();

  @Query("SELECT * FROM dose WHERE dose_id = :doseId")
  LiveData<Dose> select(long doseId);

  @Query("SELECT * FROM dose WHERE administered = 1 ORDER BY date_administered ASC")
  LiveData<List<Dose>> selectPastDoses();

  @Query("SELECT * FROM dose WHERE vaccine_id = :vaccineId ORDER BY date_administered ASC")
  LiveData<List<Dose>> selectAllDosesForVaccineId(long vaccineId);

  @Query("SELECT * FROM dose WHERE administered = 0 ORDER BY date_administered DESC LIMIT :limit")
  LiveData<List<Dose>> selectUpcomingDoses(int limit);
}
