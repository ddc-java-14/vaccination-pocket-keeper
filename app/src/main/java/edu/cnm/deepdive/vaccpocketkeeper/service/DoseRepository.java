package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.DoseSummary;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DoseRepository {

  private final Application context;
  private final DoseDao doseDao;

  public DoseRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    doseDao = database.getDoseDao();
  }

  public LiveData<Dose> get(long doseId) {
    return doseDao.select(doseId);
  }

  public LiveData<List<Dose>> getAll() {
    return doseDao.selectAll();
  }

  public LiveData<List<Dose>> getAllDosesForVaccineId(long vaccineId) {
    return doseDao.selectAllDosesForVaccineId(vaccineId);
  }

  public LiveData<List<Dose>> getPastDoses() {
    return doseDao.selectPastDoses();
  }

  public LiveData<List<Dose>> getUpcomingDoses(int limit) {
    Log.d(getClass().getSimpleName(), "size of returned list: " + String.valueOf(doseDao.selectUpcomingDoses(limit).getValue().size()));
    return doseDao.selectUpcomingDoses(limit);
  }

  public Single<Dose> save(Dose dose) {
    //FIXME Check to see if this works
    Single<Dose> task;
    if (dose.getId() == 0) {
      Dose dose2 = new Dose();
      task = doseDao
          .insert(dose2)
          .map((id) -> {
            dose2.setId(id);//id of record added, then puts dose back on the conveyor belt
            return dose2;
          });
    } else {
      task = doseDao
          .update(dose)
          .map((count) -> //count of records modified, then puts dose back on the conveyor belt
              dose
          );
    }
    return task.subscribeOn(
        Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  public Completable delete(Dose dose) {
    return (dose.getId() == 0)
        //if id is 0, then the dose has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : doseDao
            .delete(dose)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }
}
