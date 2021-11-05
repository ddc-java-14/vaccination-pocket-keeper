package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoctorDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

public class DoctorRepository {

  private final Application context;
  private final DoctorDao doctorDao;

  public DoctorRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    doctorDao = database.getDoctorDao();
  }

  public LiveData<Doctor> get(long doctorId) {
    return doctorDao.select(doctorId);
  }

  public LiveData<List<Doctor>> getAll() {
    return doctorDao.selectAll();
  }

  public Single<Doctor> save(Doctor doctor) {
    Single<Doctor> task;
    if (doctor.getId() == 0) {
      doctor.setCreated(new Date());
      task = doctorDao
          .insert(doctor)
          .map((id) -> {
            doctor.setId(id);
            return doctor;
          }); //id of record added, then puts note back on the conveyor belt
    } else {
      task = doctorDao
          .update(doctor)
          .map((count) -> //count of records modified, then puts note back on the conveyor belt
              doctor
          );
    }
    return task.subscribeOn(
        Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  public Completable delete(Doctor doctor) {
    return (doctor.getId() == 0)
        //if id is 0, then the note has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : doctorDao
            .delete(doctor)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
