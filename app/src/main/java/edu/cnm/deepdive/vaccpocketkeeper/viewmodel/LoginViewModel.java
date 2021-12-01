package edu.cnm.deepdive.vaccpocketkeeper.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.vaccpocketkeeper.service.GoogleSignInRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Implements the business logic behind the application.  Interacts with the GoogleSignInRepository to sign a User into the application.
 */
public class LoginViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final GoogleSignInRepository repository;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending; //if clear the bucket, clears all of the tasks

  /**
   * Constructor for class.  Instantiates local variables and causes a silent refresh if necessary.
   * @param application an application
   */
  public LoginViewModel(@NonNull Application application) {
    super(application);
    repository = GoogleSignInRepository.getInstance();
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    refresh(); //as soon as the view model gets loaded into memory, it will start the sign in process by refresh()
  }

  /**
   * Returns the GoogleSignInAccount.
   * @return a reactivex {@link LiveData} object of type {@link GoogleSignInAccount}.
   */
  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  /**
   * Returns the throwable local variable.
   * @return a reactivex {@link LiveData} object of type {@link Throwable}.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Interacts with the GoogleSignInRepository to refresh user credentials from the cloud.
   * If the refresh fails, it does not get reported as an error; The user has to log in again.
   */
  public void refresh() { //if refresh fails, don't report as an error, use has to log in again
    pending.add(
        repository
            .refresh()
            .subscribe(
                account::postValue, // (account) -> this.account.postValue(account)
                (throwable) -> account.postValue(null)
            )
    );
  }

  /**
   * Interacts with the GoogleSignInRepository to begins the application sign in process.
   * @param launcher an {@link ActivityResultLauncher} object of type {@link Intent}.
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    repository.startSignIn(launcher);
  }

  /**
   * Interacts with the GoogleSignInRepository to sign a user into the application.
   * @param result the result of the SignIn process
   */
  public void completeSignIn(ActivityResult result) {
    Disposable disposable = repository
        .completeSignIn(result)
        .subscribe(
            account::postValue,
            this::postThrowable
        );
    pending.add(disposable);
  }

  /**
   * Interacts with the GoogleSignInRepository to sign the User out of their Google account.
   */
  public void signOut() {
    Disposable disposable = repository
        .signOut()
        .doFinally(() -> account.postValue(null))
        .subscribe(
            () -> {
            }, //Do nothing on success
            this::postThrowable //throw an error if it fails
        );
    pending.add(disposable);
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
