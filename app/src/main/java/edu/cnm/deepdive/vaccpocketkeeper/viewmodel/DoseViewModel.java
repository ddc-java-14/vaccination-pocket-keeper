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
import java.util.List;

public class DoseViewModel extends AndroidViewModel implements LifecycleObserver {

  private final DoseRepository repository;
  private final LiveData<Dose> dose;
  private final MutableLiveData<Long> doseId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  //private final MutableLiveData<Long>
  private final SharedPreferences preferences;
  private final String futureDosesPrefKey;
  private final int futureDosesPrefDefault;
  private final MutableLiveData<Integer> futureDosesLimit;
  private final LiveData<List<Dose>> futureDoses;



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
    //sortedByTime = new MutableLiveData<>(false);
    FilterLiveData trigger =
        new FilterLiveData(futureDosesLimit);
    futureDoses = Transformations.switchMap(trigger, (params) -> repository.getUpcomingDoses(params.futureDoses));
  }

  public LiveData<Dose> getDose() {
    return dose;
  }

  public void setDoseId(long id) {
    doseId.setValue(id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  //getAll
  public LiveData<List<DoseWithDoctor>> getDoses() {
    return repository.getAll();
  }

  //getAllForVaccineId
  public LiveData<List<Dose>> getDosesForVaccineId(long id) {
    return repository.getAllDosesForVaccineId(id);
  }

  //getPastDoses
  public LiveData<List<Dose>> getPastDoses() {
    return repository.getPastDoses();
  }

  //getFutureDoses
//  public LiveData<List<Dose>> getFutureDoses() {
//    return repository.getUpcomingDoses();
//  }

  //save
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

  //delete
  public void deleteDose(Dose dose) {
    //Dose dose = new Dose();
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
    //private final boolean sortedByTime;

    private Params(int futureDoses) {
      this.futureDoses = futureDoses;
      //this.sortedByTime = sortedByTime;
    }
  }

  private static class FilterLiveData extends MediatorLiveData<Params> {

    @SuppressWarnings("ConstantConditions")
    public FilterLiveData(
        @NonNull LiveData<Integer> futureDoses
        //@NonNull LiveData<Boolean> sortedByTime
    ) {
      //combination treated as livedata rather than individual pieces
      addSource(futureDoses, (years) -> setValue(new Params(years)));
      //addSource(sortedByTime, (sorted) -> setValue(new Params(futureDoses.getValue(), sorted)));
    }
  }
}
