package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoctorDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.DoseSummary;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates a persistent DoseRepository object that allows the Dose ViewModel to interact with the DoseDao to create, read, insert, and update data to the database.
 */
public class DoseRepository {

  private final Application context;
  private final DoseDao doseDao;
  private final SharedPreferences preferences;
  private final String futureDosesPrefKey;
  private final int futureDosesPrefDefault;

  /**
   * Constructor for class that instantiates a VaccpocketkeeperDatabase database object as well as local fields and SharedPreferences.
   * @param context the application context
   */
  public DoseRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    doseDao = database.getDoseDao();

    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    Resources resources = context.getResources();
    futureDosesPrefKey = resources.getString(R.string.future_vaccine_years_pref_key);
    futureDosesPrefDefault = resources.getInteger(R.integer.future_vaccine_years_pref_default);
  }

  /**
   * Interacts with the {@link DoseDao} to get a {@link Dose} object from the database where
   * the dose_id equals the dose_id passed in from the caller.
   * @param doseId a long primitive that uniquely identifies the {@link Dose} object.
   * @return a reactivex {@link LiveData} object of type {@link Dose} object.
   */
  public LiveData<Dose> get(long doseId) {
    return doseDao.select(doseId);
  }

  /**
   * Interacts with the {@link DoseDao} to get all {@link Dose} objects from the database.
   * @return a reactivex {@link LiveData} {@link List} of {@link DoseWithDoctor} objects.
   */
  public LiveData<List<DoseWithDoctor>> getAll() {
    return doseDao.selectAll();
  }

  /**
   * Interacts with the {@link DoseDao} to get all {@link Dose} objects from the database where the vaccineId passed
   * in from the caller equals the vaccine_id of the Dose.
   * @param vaccineId a long primitive that uniquely identifies the Vaccine object.
   * @return a reactivex {@link LiveData} {@link List} of {@link Dose} objects.
   */
  public LiveData<List<Dose>> getAllDosesForVaccineId(long vaccineId) {
    return doseDao.selectAllDosesForVaccineId(vaccineId);
  }

  /**
   * Interacts with the {@link DoseDao} to get all {@link Dose} objects that were adminstered before
   * the fromDate.
   * @param fromDate a {@link Date} object that identifies the point up to which doses have been administered.
   * @return a reactivex {@link LiveData} {@link List} of {@link Dose} objects.
   */
  public LiveData<List<Dose>> getPastDoses(Date fromDate) {
    return doseDao.selectPastDoses(fromDate);
  }

  /**
   * Interacts with the {@link DoseDao} to get all {@link DoseWithDoctor} objects that are
   * coming up from this point forward to the date specified by adding the value (number of years)
   * stored in the SharedPreferences.
   * @return a reactivex {@link LiveData} {@link List} of {@link DoseWithDoctor} objects.
   */
  public LiveData<List<DoseWithDoctor>> getUpcomingDoses() {
    //Log.d(getClass().getSimpleName(), "size of returned list: " + String.valueOf(doseDao.selectUpcomingDoses(startDate, endDate).getValue().size()));
    int futureDosesPref = preferences.getInt(futureDosesPrefKey, futureDosesPrefDefault);
    Calendar calendar = Calendar.getInstance();
    Date startDate = calendar.getTime();
    calendar.add(Calendar.YEAR,futureDosesPref);
    Log.d(getClass().getSimpleName(), "Calendar: " + calendar.toString());
    Date endDate = calendar.getTime();

    return doseDao.selectUpcomingDoses(startDate, endDate);
  }

  /**
   * Interacts with the {@link DoseDao} to save a {@link Dose} object to the database.
   * If the dose_id is zero, then it creates a new {@link Dose} object;
   * Otherwise, it saves the current {@link Dose} object.
   * @param dose a {@link Dose} object that needs to be saved to the database
   * @return a reactivex {@link Single} of type {@link Dose}.
   */
  public Single<Dose> save(Dose dose) {
    Single<Dose> task;
    if (dose.getId() == 0) {
      dose.setCreated(new Date());
      task = doseDao
          .insert(dose)
          .map((id) -> {
            dose.setId(id);//id of record added, then puts dose back on the conveyor belt
            return dose;
          });
    } else {
      task = doseDao
          .update(dose)
          .map((count) -> //count of records modified, then puts dose back on the conveyor belt
              dose
          );
    }
    return task.subscribeOn(Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  /**
   * Interacts with the {@link DoseDao} to delete a {@link Dose} object from the database.
   * If the dose_id is zero, then the {@link Dose} object has not been saved to the database;
   * Then there is no point in deleting it; Otherwise, deletes the {@link Dose} object from the database.
   * @param dose a {@link Dose} object that needs to be deleted from the database
   * @return a reactivex {@link Completable}.
   */
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
