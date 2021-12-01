package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.LoginViewModel;


/**
 * Interacts with the {@link LoginViewModel} to log the user into the application. Uses the visual layout for the login screen as specified by the activity_main layout in res/layout.
 */
public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding; //when added activity login layout file, automatically got created for us
  private LoginViewModel viewModel;
  private ActivityResultLauncher<Intent> launcher;
  private boolean silent;

  /**
   * Overrides the onCreate method in AppCompatActivity.  Instantiates local variables. Specifically, starts and completes the SignIn process through screens as defined in the activity_login layout in res/layout.
   * @param savedInstanceState a {@link Bundle}.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(LoginViewModel.class); //in case of activity, always going to be this.
    getLifecycle().addObserver(viewModel); // we said that loginviewmodel implements defaultlifecycleobserver
    launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), viewModel::completeSignIn);
    silent = true;
    viewModel.getAccount().observe(this, this::handleAccount);
    viewModel.getThrowable().observe(this, this::informFailure);
  }

  private void informFailure(Throwable throwable) {
    if (throwable != null) {
      Snackbar
          .make(binding.getRoot(), R.string.login_failure_message, Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      //Switch to main activity: can put activities in navigation map, but don't want to do that, unless passing data.
      Intent intent = new Intent(this, MainActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    } else if (silent) {
      silent = false;
      binding = ActivityLoginBinding.inflate(getLayoutInflater()); //activity doesn't have a parent: therefore, different actions here
      //Attach listener to login button
      //Display a UI that has a sign-in button
      binding.signIn.setOnClickListener((v) ->
          viewModel.startSignIn(launcher));
      setContentView(binding.getRoot());
    }
  }
}