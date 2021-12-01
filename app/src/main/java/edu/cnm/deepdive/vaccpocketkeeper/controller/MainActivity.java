package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import edu.cnm.deepdive.vaccpocketkeeper.MobileNavigationDirections;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ActivityMainBinding;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.LoginViewModel;

/**
 * Interacts with the {@link LoginViewModel} to log the user into the application.
 * Uses the visual layout for the login screen as specified by the activity_login layout in res/layout.
 * Sets up the navigation structure that allows users to navigate throughout the app.
 */
public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;
  private LoginViewModel loginViewModel;
  private NavController navController;

  /**
   * Overrides the onCreate method in AppCompatActivity.  Instantiates local variables.
   * Specifically, uses layout screens as defined in the activity_main layout in res/layout.
   * Sets up the navigation structure that allows users to navigate throughout the app.
   * @param savedInstanceState a {@link Bundle}.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(loginViewModel);
    loginViewModel.getAccount().observe(this, (account) -> {
      if (account == null) {
        //switch back to Login Screen
        Intent intent = new Intent(this, LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      }
    });//in fragment: getviewlifecycleowner(), in activity: this

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.appBarMain.toolbar);
    DrawerLayout drawer = binding.drawerLayout;
    NavigationView navigationView = binding.navView;
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    appBarConfiguration = new AppBarConfiguration.Builder(
        //changes the text in the top of the screen
        R.id.nav_future_doses, R.id.nav_home, R.id.nav_vaccine, R.id.nav_camera)
        .setDrawerLayout(drawer)
        .build();
    navController = Navigation.findNavController(this,
        R.id.nav_host_fragment_content_main);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }

  /**
   * Overrides the onCreateOptionsMenu method in AppCompatActivity. Creates two menu options
   * (Sign out and Settings) as specified in the res/menu main.xml file.
   * @param menu a menu item.
   * @return a boolean representing if the menu was created successfully or not.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  /**
   *  Overrides the onOptionsItemSelected method in AppCompatActivity.  Specifies what to do if
   *  the user clicks on each menu item (Sign out versus Settings).
   * @param item a menu item.
   * @return a boolean representing if the item was handled successfully or not.
   */
    @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled;
    final int itemId = item.getItemId();
    if (itemId == R.id.sign_out) {
      loginViewModel.signOut();
      handled = true;
    } else if (itemId == R.id.action_settings) { //generated for us from overflow menu
      navController.navigate(
          MobileNavigationDirections.openSettings()); //when we created the navigation under id, followied by directions
      handled = true;
    }

    else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }


  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this,
        R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}