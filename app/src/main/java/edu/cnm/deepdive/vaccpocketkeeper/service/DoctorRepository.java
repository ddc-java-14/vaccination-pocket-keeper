package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoctorDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates a persistent DoctorRepository object that allows the Doctor ViewModel to interact with the DoctorDao to create, read, insert, and update data to the database.
 */
public class DoctorRepository {

  private final Application context;
  private final DoctorDao doctorDao;

  /**
   * Constructor for class that instantiates a VaccpocketkeeperDatabase database object as well as local fields.
   * @param context the application context
   */
  public DoctorRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    doctorDao = database.getDoctorDao();
  }

  /**
   * Interacts with the {@link DoctorDao} to get a {@link Doctor} object from the database where
   * the doctor_id equals the doctor_id passed in from the caller.
   * @param doctorId a long primitive that uniquely identifies the {@link Doctor} object.
   * @return  a reactivex {@link LiveData} object of type {@link Doctor} object.
   */
  public LiveData<Doctor> get(long doctorId) {
    return doctorDao.select(doctorId);
  }

  /**
   * Interacts with the {@link DoctorDao} to get all {@link Doctor} objects from the database.
   * @return a reactivex {@link LiveData} {@link List} of {@link Doctor} objects.
   */
  public LiveData<List<Doctor>> getAll() {
    return doctorDao.selectAll();
  }

  /**
   * Interacts with the {@link DoctorDao} to save a {@link Doctor} object to the database.
   * If the doctor_id is zero, then it creates a new {@link Doctor} object;
   * Otherwise, it saves the current {@link Doctor} object.
   * @param doctor a {@link Doctor} object that needs to be saved to the database
   * @return a reactivex {@link Single} of type {@link Doctor}.
   */
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
    return task.subscribeOn(Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  /**
   * Interacts with the {@link DoctorDao} to delete a {@link Doctor} object from the database.
   * If the doctor_id is zero, then the {@link Doctor} object has not been saved to the database;
   * Then there is no point in deleting it; Otherwise, deletes the {@link Doctor} object from the database.
   * @param doctor a {@link Doctor} object that needs to be deleted from the database
   * @return a reactivex {@link Completable}.
   */
  public Completable delete(Doctor doctor) {
    return (doctor.getId() == 0)
        //if id is 0, then the doctor has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : doctorDao
            .delete(doctor)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
