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
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.service.VaccineRepository;
import io.reactivex.disposables.CompositeDisposable;

public class VaccineViewModel extends AndroidViewModel implements LifecycleObserver {

  private final VaccineRepository repository;
  private final MutableLiveData<Vaccine> vaccine;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public VaccineViewModel(@NonNull Application application) {
    super(application);
    repository = new VaccineRepository();
    vaccine = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    insertNewVaccine();
  }

  public LiveData<Vaccine> getVaccine() {
    return vaccine;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void insertNewVaccine() {
    throwable.postValue(null);
    pending.add(
        repository
            .addNewVaccine()
            .subscribe(
                this.vaccine::postValue,
                this::postThrowable
            ) //consumer of a game, consumer of a throwable; receive a throwable object and then do postThrowable object.
    );
  }

  public void addDosesForVaccine() {
    throwable.postValue(null); //clears the exceptions.
    pending.add(
        repository.addDosesForVaccine(vaccine.getValue())
            .subscribe(
                vaccine::postValue,
                this::postThrowable //if we didn't get a POST? postValue, then get a throwable
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(),throwable.getMessage(),throwable);
    this.throwable.postValue(throwable);
  }
}
