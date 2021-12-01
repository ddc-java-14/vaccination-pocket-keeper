package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.annotation.NonNull;
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

public class VaccineRepository {

  private final Application context;
  private final VaccineDao vaccineDao;
  private final DoseDao doseDao;

  public VaccineRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    vaccineDao = database.getVaccineDao();
    doseDao = database.getDoseDao();
  }

  public LiveData<VaccineWithDoses> get(long vaccineId) {
    return vaccineDao.select(vaccineId);
  }

  public LiveData<VaccineWithDoses> getVaccine(long vaccineId) {
    return vaccineDao.select(vaccineId);
  }

  public LiveData<List<Vaccine>> getAll() {
    return vaccineDao.selectAll();
  }

  public LiveData<List<VaccineSummary>> getPastVaccines() {
    return vaccineDao.selectPastVaccines();
  }

  public LiveData<List<VaccineSummary>> getUpcomingVaccines() {
    return vaccineDao.selectUpcomingVaccines();
  }

  public Single<VaccineWithDoses> save(VaccineWithDoses vaccine) {
    Single<VaccineWithDoses> task;
    if (vaccine.getId() == 0) {//creating a new vaccine
      vaccine.setCreated(new Date());
      Calendar cal = Calendar.getInstance();
      for (int i = 0; i < vaccine.getTotalNumberOfDoses(); i++) { //TODO: getTotalNumberOfDoses is empty
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

  public Completable delete(Vaccine vaccine) {
    return (vaccine.getId() == 0) //if id is 0, then the note has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : vaccineDao
            .delete(vaccine)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
