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
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.service.DoctorRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

/**
 * Implements the business logic behind the application.  Interacts with the DoctorRepository to perform CRUD operations on the database.
 */
public class DoctorViewModel extends AndroidViewModel implements LifecycleObserver {

  private final DoctorRepository repository;
  private final LiveData<Doctor> doctor;
  private final MutableLiveData<Long> doctorId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  /**
   * Class constructor.  Instantiates local class variables. Additionally, implements a switchmap that
   * invokes a get operation on the database to get an object of type {@link Doctor} whenever the contents
   * of the doctorId changes.
   * @param application an application.
   */
  public DoctorViewModel(@NonNull Application application) {
    super(application);
    repository = new DoctorRepository(application);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    doctorId = new MutableLiveData<>();
    //whenever the contents of this doctorId changes, it invokes the lambda
    doctor = Transformations.switchMap(doctorId, doctorId1 -> repository.get(doctorId1)
        //but query doesn't execute unless someone is observing that livedata.
    );//triggers a refresh of live data
  }

  /**
   * Returns the local doctor variable.
   * @return a reactivex {@link LiveData} object of type {@link Doctor}.
   */
  public LiveData<Doctor> getDoctor() {
    return doctor;
  }

  /**
   * Sets the local doctorId variable. If an object is observing this, it will cause a refresh of doctor assignment in constructor.
   * @param doctorId a unique identifier for a {@link Doctor} object.
   */
  public void setDoctorId(long doctorId) {
    this.doctorId.setValue(doctorId);//if someone is observing this, it will cause a refresh of doctor assignment in constructor
  }

  /**
   * Returns the local variable throwable.
   * @return the local variable throwable.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Interacts with the DoctorRepository to get a {@link Doctor} object as specified by the id parameter.
   * @param id a unique identifier for a {@link Doctor} object.
   * @return a reactivex {@link LiveData} object of type {@link Doctor}.
   */
  public LiveData<Doctor> getDoctorById(long id) {
    return repository.get(id);
  }

  /**
   * Interacts with the DoctorRepository to get a list of all {@link Doctor} objects in the database.
   * @return a reactivex {@link LiveData} {@link List} object of type {@link Doctor}.
   */
  public LiveData<List<Doctor>> getDoctors() {
    return repository.getAll();
  }

  /**
   * Interacts with the DoctorRepository to get save a {@link Doctor} object to the database.
   * @param doctor the {@link Doctor} object to be saved.
   */
  public void save(Doctor doctor) {
    pending.add(
        repository
            .save(doctor)
            .subscribe(
                (savedDoctor) -> {},
                this::postThrowable //replace expression lambda with method reference lambda.
            ) //causes this to execute; first parameter is consume of succesful results, second is consumer of unsucessful result

    );
  }

  /**
   * Interacts with the DoctorRepository to get delete a {@link Doctor} object from the database.
   * @param doctor the {@link Doctor} object to be deleted.
   */
  public void deleteDoctor(Doctor doctor) {
    //Vaccine vaccine = new Vaccine();
    throwable.postValue(null);
    pending.add(
        repository
            .delete(doctor)
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
