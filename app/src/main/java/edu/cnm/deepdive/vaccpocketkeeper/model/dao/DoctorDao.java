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

@Dao
public interface DoctorDao {

  @Insert
  Single<Long> insert(Doctor doctor);

  @Insert
  Single<List<Long>> insert(Doctor... doctors);

  @Insert
  Single<List<Long>> insert(Collection<Doctor> doctors);

  @Update
  Single<Integer> update(Doctor doctor);//return type is number of records affected by the operation

  @Update
  Single<Integer> update(Doctor... doctors);

  @Update
  Single<Integer> update(Collection<Doctor> doctors);

  @Delete
  Single<Integer> delete(Doctor doctor);

  @Delete
  Single<Integer> delete(Doctor... doctors);

  @Delete
  Single<Integer> delete(Collection<Doctor> doctors);

  @Query("SELECT * FROM doctor ORDER BY created DESC") //we named our table doctor
  LiveData<List<Doctor>> selectAll();

  @Query("SELECT * FROM doctor WHERE doctor_id = :doctorId")
  LiveData<Doctor> select(long doctorId );
}
