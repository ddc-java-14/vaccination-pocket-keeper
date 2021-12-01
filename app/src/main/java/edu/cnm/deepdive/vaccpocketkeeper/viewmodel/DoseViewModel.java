package edu.cnm.deepdive.vaccpocketkeeper.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.service.DoseRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Date;
import java.util.List;

/**
 * Implements the business logic behind the application.  Interacts with the DoseRepository to perform CRUD operations on the database.
 */
public class DoseViewModel extends AndroidViewModel implements LifecycleObserver {

  private final DoseRepository repository;
  private final LiveData<Dose> dose;
  private final MutableLiveData<Long> doseId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final SharedPreferences preferences;
  private final String futureDosesPrefKey;
  private final int futureDosesPrefDefault;
  private final MutableLiveData<Integer> futureDosesLimit;

  /**
   * Class constructor.  Instantiates local class variables. Additionally, reads future dose
   * values from SharedPreferences.
   * @param application an application.
   */
  public DoseViewModel(@NonNull Application application) {
    super(application);
    repository = new DoseRepository(application);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    doseId = new MutableLiveData<>();
    //whenever the contents of this doseId changes, it invokes the lambda
    dose = Transformations.switchMap(doseId, repository::get
        //but query doesn't execute unless someone is observing that livedata.
    );//triggers a refresh of live data
    //vaccineId = args.getVaccineId();
    preferences = PreferenceManager.getDefaultSharedPreferences(application);
    Resources resources = application.getResources();
    futureDosesPrefKey = resources.getString(R.string.future_doses_pref_key);
    futureDosesPrefDefault = resources.getInteger(R.integer.future_doses_pref_default);
    int futureDosesPref = preferences.getInt(futureDosesPrefKey, futureDosesPrefDefault);
    futureDosesLimit = new MutableLiveData<>(futureDosesPref);
  }

  /**
   * Returns the local dose variable.
   * @return a reactivex {@link LiveData} object of type {@link Dose}.
   */
  public LiveData<Dose> getDose() {
    return dose;
  }

  /**
   * Interacts with the DoseRepository to get a {@link Dose} object as specified by the doseId parameter.
   * @param doseId a unique identifier for a {@link Dose} object.
   * @return a reactivex {@link LiveData} object of type {@link Dose}.
   */
  public LiveData<Dose> getDoseById(long doseId) {
    return repository.get(doseId);
  }

  /**
   * Sets the local doseId variable. If an object is observing this, it will cause a refresh of dose assignment in constructor.
   * @param id a unique identifier for a {@link Dose} object.
   */
  public void setDoseId(long id) {
    doseId.setValue(id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  /**
   * Returns the local variable throwable.
   * @return the local variable throwable.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Interacts with the DoseRepository to get a list of all upcoming {@link Dose} objects in the database.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link DoseWithDoctor}.
   */
  public LiveData<List<DoseWithDoctor>> getDoses() {
    return repository.getUpcomingDoses();
  }

  /**
   * Interacts with the DoseRepository to get a list of all {@link Dose} objects that belong to the vaccine specified by the
   * vaccineId in the database.
   * @param vaccineId a unique identifier for a Vaccine object.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link Dose}.
   */
  public LiveData<List<Dose>> getDosesForVaccineId(long vaccineId) {
    return repository.getAllDosesForVaccineId(vaccineId);
  }

  /**
   * Interacts with the DoseRepository to get a list of all past {@link Dose} objects
   * before the specified date.
   * @param fromDate a {@link Date} before which we are getting past {@link Dose} objects.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link Dose}.
   */
  public LiveData<List<Dose>> getPastDoses(Date fromDate) {
    return repository.getPastDoses(fromDate);
  }

  /**
   * Interacts with the DoseRepository to get save a {@link Dose} object to the database.
   * @param dose the {@link Dose} object to be saved.
   */
  public void save(Dose dose) {
    pending.add(
        repository
            .save(dose)
            .subscribe(
                (savedDose) -> {},
                this::postThrowable //replace expression lambda with method reference lambda.
            ) //causes this to execute; first parameter is consume of succesful results, second is consumer of unsucessful result
    );
  }

  /**
   * Interacts with the DoseRepository to get delete a {@link Dose} object from the database.
   * @param dose the {@link Dose} object to be deleted.
   */
  public void deleteDose(Dose dose) {
    throwable.postValue(null);
    pending.add(
        repository
            .delete(dose)
            .subscribe(
                () -> {},
                this::postThrowable
            )
        //consumer of a game, consumer of a throwable; receive a throwable object and then do postThrowable object.
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  private static class Params {

    private final int futureDoses;

    private Params(int futureDoses) {
      this.futureDoses = futureDoses;
    }
  }

  private static class FilterLiveData extends MediatorLiveData<Params> {

    /**
     * Constructor for class.  Specifies a combination of data that is to be treated as livedata
     * rather than individual pieces
     * @param futureDoses a reactivex {@link LiveData} object of type {@link Integer}.
     */
    @SuppressWarnings("ConstantConditions")
    public FilterLiveData(
        @NonNull LiveData<Integer> futureDoses
    ) {
      //combination treated as livedata rather than individual pieces
      addSource(futureDoses, (years) -> setValue(new Params(years)));
    }
  }
}
