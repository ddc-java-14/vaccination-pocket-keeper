package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.adapter.AllFutureDosesAdapter;
import edu.cnm.deepdive.vaccpocketkeeper.adapter.DoseAdapter;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentAllFutureDosesBinding;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoseViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.VaccineViewModel;

/**
 * Implements the layout and functionality associated with displaying a list of all future Doses to the user.
 * Uses the layout as specified in res/layout/fragment_all_future_doses.xml.
 */
public class AllFutureDosesFragment extends Fragment {

  private DoseViewModel doseViewModel;
  private VaccineViewModel vaccineViewModel;
  private FragmentAllFutureDosesBinding binding;
  private long vaccineId;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  /**
   * Overrides the onCreate method in Fragment. Inflates (sets up and displays) the layout as
   * specified in fragment_all_future_doses.xml.
   * @param inflater a {@link LayoutInflater}.
   * @param container a {@link ViewGroup}.
   * @param savedInstanceState a {@link Bundle}.
   * @return a {@link View}.
   */
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentAllFutureDosesBinding.inflate(inflater, container, false);
    //binding.submit.setOnClickListener((v) ->
    //    viewModel.submitGuess(binding.guess.getText().toString().trim()));
    //binding.guess.setFilters(new InputFilter[]{this});
    //compiler infers that v is a view : (View v)
    return binding.getRoot();
  }

  /**
   * Overrides the onViewCreated method in Fragment.  Specifically, interacts with the
   * {@link DoseViewModel} to get a list of all future Doses from the database.
   * @param view a {@link View}.
   * @param savedInstanceState a {@link Bundle}.
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
//    viewModel = new ViewModelProvider(getActivity()).get(DoseViewModel.class);
//    getLifecycle().addObserver(viewModel);
//    viewModel.getThrowable().observe(getViewLifecycleOwner(), this::displayError);
//    viewModel.getDoses().observe(getViewLifecycleOwner(), this::update); //observes a dose
//    doseViewModel = new ViewModelProvider(this).get(DoseViewModel.class);
//    doseViewModel
//        .getDosesForVaccineId(vaccineId)
//        .observe(getViewLifecycleOwner(),(doses) -> {
//          DoseAdapter adapter = new DoseAdapter(getContext(), doses, this::editDose,
//              (dose,v) -> doseViewModel.deleteDose(dose));
//          binding.doses.setAdapter(adapter);
//        });
    doseViewModel = new ViewModelProvider(this).get(DoseViewModel.class);
    doseViewModel
        .getDoses()
        .observe(getViewLifecycleOwner(), (doses) -> {
          AllFutureDosesAdapter adapter = new AllFutureDosesAdapter(getContext(), doses, this::editDose,
              (dose,v) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                        doseViewModel.deleteDose(dose);
                      }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                      }
                    });
                AlertDialog alert = builder.create();
                alert.show();
              });
          binding.doses.setAdapter(adapter);
        });
  } //when fragment dies, then cleans up

//  @Override
//  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//    super.onCreateOptionsMenu(menu, inflater);
//    inflater.inflate(R.menu.play_actions, menu);
//  }

//  @Override
//  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//    //return true if menu item that we handled, otherwise return false
//    boolean handled;
//    if (item.getItemId() == R.id.new_game) {
//      handled = true;
//      binding.guess.getText().clear();
//      viewModel.startGame();
//    } else {
//      handled = super.onOptionsItemSelected(item);
//    }
//    return handled;
//  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

//  private void update(Dose dose) { //updates the display model
//    DoseAdapter adapter = new DoseAdapter(getContext(),
//        dose.getName());// how we get a context in a fragment
//    binding.dose.setAdapter(adapter);//this adapter can tell us our guesses
//  }

  private void displayError(Throwable throwable) {
    if (throwable != null) {
      Snackbar snackbar = Snackbar.make(binding.getRoot(),
          getString(R.string.error_message, throwable.getMessage()),
          Snackbar.LENGTH_INDEFINITE);
      snackbar.setAction(R.string.error_dismiss,
          (v) -> snackbar.dismiss());//gets access to livedata from viewmodel - just observing, run contents of bucket once this object changes.
      snackbar.show();
    }
  }

  private void editDose(long id, View view) {
    Navigation.findNavController(binding.getRoot())
        .navigate(AllFutureDosesFragmentDirections.openDose().setDoseId(id));
  }

}