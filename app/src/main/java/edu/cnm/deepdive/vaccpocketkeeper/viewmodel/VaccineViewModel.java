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
import java.util.Date;
import java.util.List;

/**
 * Implements the business logic behind the application.  Interacts with the VaccineRepository to perform CRUD operations on the database.
 */
public class VaccineViewModel extends AndroidViewModel implements LifecycleObserver {

  private final VaccineRepository repository;
  private final LiveData<VaccineWithDoses> vaccine;
  private final MutableLiveData<Long> vaccineId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  /**
   * Class constructor.  Instantiates local class variables. Additionally, implements a switchmap that
   * invokes a get operation on the database to get an object of type {@link Vaccine} whenever the contents
   * of the vaccineId changes.
   * @param application an application.
   */
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

  /**
   * Returns the local vaccine variable.
   * @return a reactivex {@link LiveData} object of type {@link VaccineWithDoses}.
   */
  public LiveData<VaccineWithDoses> getVaccine() {
    return vaccine;
  }

  /**
   * Returns the name of the vaccine of the local vaccine variable.
   * @return an object of type {@link String}.
   */
  public String getVaccineName() {
    return vaccine.getValue().getName();
  }

  /**
   * Sets the local vaccineId variable. If an object is observing this, it will cause a refresh of vaccine assignment in constructor.
   * @param vaccineId a unique identifier for a {@link Vaccine} object.
   */
  public void setVaccineId(long vaccineId) {
    this.vaccineId.setValue(vaccineId);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  /**
   * Returns the local variable throwable.
   * @return the local variable throwable.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Interacts with the VaccineRepository to get a list of all {@link Vaccine} objects in the database.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link Vaccine}.
   */
  public LiveData<List<Vaccine>> getVaccines() {
    return repository.getAll();
  }

  /**
   * Interacts with the VaccineRepository to get a list of all past {@link Vaccine} objects
   * before the specified date.
   * @param fromDate a {@link Date} before which we are getting past {@link Vaccine} objects.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link VaccineSummary}.
   */
  public LiveData<List<VaccineSummary>> getPastVaccines(Date fromDate) {
    return repository.getPastVaccines(fromDate);
  }

  /**
   * Interacts with the VaccineRepository to get a list of all past {@link Vaccine} objects
   * after the specified date.
   * @param fromDate a {@link Date} after which we are getting future {@link Vaccine} objects.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link VaccineSummary}.
   */
  public LiveData<List<VaccineSummary>> getFutureVaccines(Date fromDate) {
    return repository.getUpcomingVaccines(fromDate);
  }

  /**
   * Interacts with the VaccineRepository to get save a {@link Vaccine} object to the database.
   * @param vaccine the {@link Vaccine} object to be saved.
   */
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

  /**
   * Interacts with the VaccineRepository to get delete a {@link Vaccine} object from the database.
   * @param vaccine the {@link Vaccine} object to be deleted.
   */
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
