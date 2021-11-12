package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class GoogleSignInRepository {

  private static Application context;

  private final GoogleSignInClient client;

  private GoogleSignInAccount account;

  private GoogleSignInRepository() {
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId() //oauth key from google
        .requestProfile()
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  public static void setContext(Application context) {
    GoogleSignInRepository.context = context;
  }

  public static GoogleSignInRepository getInstance() {
    return InstanceHolder.INSTANCE; //here is where this class gets loaded into memory, not when GoogleSignIn class is loaded
  }

  public Single<GoogleSignInAccount> refresh() {
    return Single
        .create((SingleOnSubscribe<GoogleSignInAccount>) (emitter) ->
            client
                .silentSignIn()
                .addOnSuccessListener(this::setAccount)
                .addOnSuccessListener(emitter::onSuccess)
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());
  }

  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single
        .create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            GoogleSignInAccount account = task.getResult(ApiException.class);
            setAccount(account);
            emitter.onSuccess(account);
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .observeOn(Schedulers.io());
  }

  public Completable signOut() {
    return Completable
        .create((emitter) ->
            client
                .signOut() //sign out gives us a void = null, which we don't care about
                .addOnCompleteListener((ignored) -> {
                  setAccount(null);
                  emitter.onComplete();
                })
                .addOnFailureListener(emitter::onError)
        )
        .subscribeOn(Schedulers.io());
  }

  private void setAccount(GoogleSignInAccount account) {
    this.account = account;
    if (account != null) {
      Log.d(getClass().getSimpleName(),
          "account successfully set: " + account.getDisplayName());
    }
  }

  private static class InstanceHolder {
    private static final GoogleSignInRepository INSTANCE = new GoogleSignInRepository();
  }
}
