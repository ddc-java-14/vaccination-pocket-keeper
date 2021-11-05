package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
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
  Single<List<Long>> insert(Collection<Dose> doses);

  @Update
  Single<Integer> update(Dose dose);//return type is number of records affected by the operation

  @Update
  Single<Integer> update(Dose... doses);

  @Update
  Single<Integer> update(Collection<Dose> doses);

  @Delete
  Single<Integer> delete(Dose dose);

  @Delete
  Single<Integer> delete(Dose... doses);

  @Delete
  Single<Integer> delete(Collection<Dose> doses);

  @Query("SELECT * FROM dose ORDER BY created DESC") //we named our table dose
  LiveData<List<Dose>> selectAll();

  @Query("SELECT * FROM dose WHERE dose_id = :doseId")
  LiveData<Dose> select(long doseId );

  @Query("SELECT * FROM dose_summary WHERE administered = :isAdministered ORDER BY date_administered DESC")
  LiveData<List<VaccineSummary>> selectPastVaccines(boolean isAdministered);

  @Query("SELECT * FROM dose_summary WHERE administered = :isAdministered ORDER BY date_administered DESC")
  LiveData<List<VaccineSummary>> selectUpcomingVaccines(boolean isAdministered);
}
