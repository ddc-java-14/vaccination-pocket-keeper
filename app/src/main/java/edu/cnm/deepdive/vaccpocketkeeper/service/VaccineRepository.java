package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VaccineRepository {

  private final Application context;
  private final VaccineDao vaccineDao;

  public VaccineRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    vaccineDao = database.getVaccineDao();
  }

  public LiveData<VaccineWithDoses> get(long vaccineId) {
    return vaccineDao.select(vaccineId);
  }

  public LiveData<List<Vaccine>> getAll() {
    return vaccineDao.selectAll();
  }

  public LiveData<List<VaccineSummary>> getPastVaccines() {
    return vaccineDao.selectPastVaccines(true);
  }

  public LiveData<List<VaccineSummary>> getUpcomingVaccines() {
    return vaccineDao.selectUpcomingVaccines(false);
  }

  public Single<Vaccine> save(Vaccine vaccine) {
    Single<Vaccine> task;
    if (vaccine.getId() == 0) {
      vaccine.setCreated(new Date());
      Calendar cal = Calendar.getInstance();
      for (int i = 0; i < vaccine.getTotalNumberOfDoses(); i++) { //TODO: getTotalNumberOfDoses is empty
        Dose dose = new Dose();
        cal.add(Calendar.YEAR, vaccine.getFrequency());
        dose.setDateAdministered(cal.getTime());
        vaccine.getDoses().add(dose);
      }
      task = vaccineDao
          .insert(vaccine)
          .map((id) -> {
            vaccine.setId(id);
            return vaccine;
          }); //id of record added, then puts note back on the conveyor belt
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
