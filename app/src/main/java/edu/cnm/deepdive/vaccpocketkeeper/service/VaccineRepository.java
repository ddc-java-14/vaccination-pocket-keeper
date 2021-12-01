package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates a persistent VaccineRepository object that allows the Vaccine ViewModel to interact with the DoseDao to create, read, insert, and update data to the database.
 */
public class VaccineRepository {

  private final Application context;
  private final VaccineDao vaccineDao;
  private final DoseDao doseDao;

  /**
   * Constructor for class that instantiates a VaccpocketkeeperDatabase database object as well as local fields.
   * @param context the application context
   */
  public VaccineRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    vaccineDao = database.getVaccineDao();
    doseDao = database.getDoseDao();
  }

  /**
   * Interacts with the {@link VaccineDao} to get a {@link Vaccine} object from the database where
   * the vaccine_id equals the vaccine_id passed in from the caller.
   * @param vaccineId a long primitive that uniquely identifies the {@link Vaccine} object.
   * @return a reactivex {@link LiveData} object of type {@link VaccineWithDoses} object.
   */
  public LiveData<VaccineWithDoses> get(long vaccineId) {
    return vaccineDao.select(vaccineId);
  }

  /**
   * Interacts with the {@link VaccineDao} to get all {@link Vaccine} objects from the database.
   * @return a reactivex {@link LiveData} {@link List} of {@link Vaccine} objects.
   */
  public LiveData<List<Vaccine>> getAll() {
    return vaccineDao.selectAll();
  }

  /**
   * Interacts with the {@link VaccineDao} to get all {@link Vaccine} objects that were adminstered before
   * the fromDate.
   * @param fromDate a {@link Date} object that identifies the point up to which vaccine have been created.
   * @return a reactivex {@link LiveData} {@link List} of {@link VaccineSummary} objects.
   */
  public LiveData<List<VaccineSummary>> getPastVaccines(Date fromDate) {
    return vaccineDao.selectPastVaccines(fromDate);
  }

  /**
   * Interacts with the {@link VaccineDao} to get all {@link VaccineSummary} objects that are
   * coming up from this point forward.
   * @return a reactivex {@link LiveData} {@link List} of {@link VaccineSummary} objects.
   */
  public LiveData<List<VaccineSummary>> getUpcomingVaccines(Date fromDate) {
    return vaccineDao.selectUpcomingVaccines(fromDate);
  }

  /**
   * Interacts with the {@link VaccineDao} to save a {@link Vaccine} object to the database.
   * If the vaccine_id is zero, then it creates a new {@link Vaccine} object and all corresponding
   * {@link Dose} objects totalling the toal number of doses specified in the vaccine object;
   * Otherwise, it saves the current {@link Vaccine} object.
   * @param vaccine a {@link Vaccine} object that needs to be saved to the database
   * @return a reactivex {@link Single} of type {@link VaccineWithDoses}.
   */
  public Single<VaccineWithDoses> save(VaccineWithDoses vaccine) {
    Single<VaccineWithDoses> task;
    if (vaccine.getId() == 0) {//creating a new vaccine
      vaccine.setCreated(new Date());
      Calendar cal = Calendar.getInstance();
      for (int i = 0; i < vaccine.getTotalNumberOfDoses(); i++) {
        DoseWithDoctor dose = new DoseWithDoctor();
        cal.add(Calendar.YEAR, vaccine.getFrequency());
        dose.setDateAdministered(cal.getTime());
        if (vaccine.getName().length() >= 4) {
          dose.setName(vaccine.getName().substring(0, 4) + ":DoseNumber" + (i + 1));
        } else {
          dose.setName(vaccine.getName() + ":DoseNumber" + (i + 1));
        }
        vaccine.getDoses().add(dose);
      }
      task = vaccineDao
          .insert(vaccine)
          .map((id) -> {
            vaccine.setId(id);//id of record added, then puts note back on the conveyor belt
            for (Dose dose: vaccine.getDoses()) {
              dose.setVaccineId(id);
            }
            return vaccine;
          })
          .flatMap((savedVaccine) -> doseDao.insert(savedVaccine.getDoses()))
          .map((ids) -> {
            Iterator<Long> idIterator = ids.iterator();
            Iterator<DoseWithDoctor> doseIterator = vaccine.getDoses().iterator();
            while (idIterator.hasNext()) {
              Dose dose = doseIterator.next();
              Long id = idIterator.next();
              dose.setId(id);
            }
            return vaccine;
          });
    } else {
      task = vaccineDao
          .update(vaccine)
          .map((count) -> //count of records modified, then puts note back on the conveyor belt
              vaccine
          );
    }
    return task.subscribeOn(Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  /**
   * Interacts with the {@link VaccineDao} to delete a {@link Vaccine} object from the database.
   * If the vaccine_id is zero, then the {@link Vaccine} object has not been saved to the database;
   * Then there is no point in deleting it; Otherwise, deletes the {@link Vaccine} object from the database.
   * @param vaccine a {@link Vaccine} object that needs to be deleted from the database
   * @return a reactivex {@link Completable}.
   */
  public Completable delete(Vaccine vaccine) {
    return (vaccine.getId() == 0) //if id is 0, then the note has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : vaccineDao
            .delete(vaccine)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
