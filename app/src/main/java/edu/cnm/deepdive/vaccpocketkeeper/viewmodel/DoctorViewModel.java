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
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoctorWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import edu.cnm.deepdive.vaccpocketkeeper.service.DoctorRepository;
import edu.cnm.deepdive.vaccpocketkeeper.service.VaccineRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class DoctorViewModel extends AndroidViewModel implements LifecycleObserver {

  private final DoctorRepository repository;
  private final LiveData<Doctor> doctor;
  private final MutableLiveData<Long> doctorId;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public DoctorViewModel(@NonNull Application application) {
    super(application);
    repository = new DoctorRepository(application);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    doctorId = new MutableLiveData<>();
    //whenever the contents of this vaccineId changes, it invokes the lambda
    doctor = Transformations.switchMap(doctorId, doctorId1 -> repository.get(doctorId1)
        //but query doesn't execute unless someone is observing that livedata.
    );//triggers a refresh of live data
  }

  public LiveData<Doctor> getDoctor() {
    return doctor;
  }

  public void setDoctorId(long id) {
    doctorId.setValue(
        id);//if someone is observing this, it will cause a refresh of note assignment in constructor
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Doctor> getDoctorById(long id) {
    return repository.get(id);
  }

  //getAll
  public LiveData<List<Doctor>> getDoctors() {
    return repository.getAll();
  }

  //save
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

  //delete
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
