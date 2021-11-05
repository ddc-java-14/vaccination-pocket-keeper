package edu.cnm.deepdive.vaccpocketkeeper.service;

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

  private final DoctorDao doctorDao;
  private final DoseDao doseDao;
  
  public DoctorRepository() {
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    doctorDao = database.getDoctorDao();
    doseDao = database.getDoseDao();
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

//  public Single<Doctor> addNewDose() {
//    return Single
//        .fromCallable(() -> {
//          Doctor doctor = new Doctor();
//          return doctor;
//        }) //asynchronous call
//        .subscribeOn(Schedulers.io());
//  }

  public Single<Doctor> addDosesForDoctor(Doctor doctor) {
    return Single
        .fromCallable(() -> {
          Dose dose = new Dose();
          doctor.getDoses().add(dose);
          return doctor;
        })
        .flatMap(this::addDoctorWithDoses)
        .subscribeOn(Schedulers.io());
  }

  @NonNull
  private Single<Doctor> addDoctorWithDoses(Doctor doctor) {
    return doctorDao
        .insert(doctor)
        .map((id) -> {
          doctor.setId(id);
          for (Dose dose : doctor.getDoses()) {
            dose.setId(id);
          }
          return doctor;
        })
        .flatMap((doctor2) -> doseDao
            .insert(doctor2.getDoses())
            //TODO invoke Dose.setId for all of the doses
            .map((ids) -> doctor2));
    //: Single.just(dose);
  }
}
