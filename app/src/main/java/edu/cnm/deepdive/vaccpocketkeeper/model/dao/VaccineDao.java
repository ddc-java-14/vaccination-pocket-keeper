package edu.cnm.deepdive.vaccpocketkeeper.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface VaccineDao {

  @Insert
  Single<Long> insert(Vaccine vaccine);

  @Insert
  Single<List<Long>> insert(Vaccine... vaccines);

  @Insert
  Single<List<Long>> insert(Collection<Vaccine> vaccines);

  @Update
  Single<Integer> update(Vaccine vaccine);//return type is number of records affected by the operation

  @Update
  Single<Integer> update(Vaccine... vaccines);

  @Update
  Single<Integer> update(Collection<Vaccine> vaccines);

  @Delete
  Single<Integer> delete(Vaccine vaccine);

  @Delete
  Single<Integer> delete(Vaccine... vaccines);

  @Delete
  Single<Integer> delete(Collection<Vaccine> vaccines);

  @Query("SELECT * FROM vaccine ORDER BY created DESC") //we named our table vaccine
  LiveData<List<Vaccine>> selectAll();

  @Transaction
  @Query("SELECT * FROM vaccine WHERE vaccine_id = :vaccineId")
  LiveData<VaccineWithDoses> select(long vaccineId);

  @Transaction
  @Query("SELECT * FROM vaccine_summary WHERE administered = 1 ORDER BY date_administered DESC")
  LiveData<List<VaccineSummary>> selectPastVaccines();

  @Transaction
  @Query("SELECT * FROM vaccine_summary WHERE administered = 0 ORDER BY date_administered DESC LIMIT :limit")
  LiveData<List<VaccineSummary>> selectUpcomingVaccines(int limit);

}
