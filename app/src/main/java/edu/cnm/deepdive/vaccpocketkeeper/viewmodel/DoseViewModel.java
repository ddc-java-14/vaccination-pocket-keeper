package edu.cnm.deepdive.vaccpocketkeeper.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.DoseSummary;
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
  }

  public LiveData<Dose> getDose() {
    return dose;
  }

  public void setDoseId(long id) {
    doseId.setValue(
        id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  //getAll
  public LiveData<List<Dose>> getDoses() {
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
  public LiveData<List<Dose>> getFutureDoses() {
    return repository.getUpcomingDoses();
  }

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
}
