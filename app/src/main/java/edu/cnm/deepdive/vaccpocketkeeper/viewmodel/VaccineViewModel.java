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
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import edu.cnm.deepdive.vaccpocketkeeper.service.VaccineRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class VaccineViewModel extends AndroidViewModel implements LifecycleObserver {

  private final VaccineRepository repository;
  private final LiveData<VaccineWithDoses> vaccine;
  private final MutableLiveData<Long> vaccineId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public VaccineViewModel(@NonNull Application application) {
    super(application);
    repository = new VaccineRepository(application);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    vaccineId = new MutableLiveData<>();
    //whenever the contents of this vaccineId changes, it invokes the lambda
    vaccine = Transformations.switchMap(vaccineId, repository::get
        //but query doesn't execute unless someone is observing that livedata.
    );//triggers a refresh of live data
  }

  public LiveData<VaccineWithDoses> getVaccine() {
    return vaccine;
  }

  public String getVaccineName(long vaccineId) {
    return vaccine.getValue().getName();
  }

  public void setVaccineId(long id) {
    vaccineId.setValue(
        id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  //getAll
  public LiveData<List<Vaccine>> getVaccines() {
    return repository.getAll();
  }

  //getPastVaccines
  public LiveData<List<VaccineSummary>> getPastVaccines() {
    return repository.getPastVaccines();
  }

  //getFutureVaccines
  public LiveData<List<VaccineSummary>> getFutureVaccines() {
    return repository.getUpcomingVaccines();
  }

  //save
  public void save(VaccineWithDoses vaccine) {
    pending.add(
        repository
            .save(vaccine)
            .subscribe(
                (savedVaccine) -> {},
                this::postThrowable //replace expression lambda with method reference lambda.
            ) //causes this to execute; first parameter is consume of succesful results, second is consumer of unsucessful result
    );
  }

  //delete
  public void deleteVaccine(Vaccine vaccine) {
    //Vaccine vaccine = new Vaccine();
    throwable.postValue(null);
    pending.add(
        repository
            .delete(vaccine)
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
