package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.DialogInterface;
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
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.adapter.DoctorAdapter;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoctorBinding;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.VaccineViewModel;

/**
 * Implements the layout and functionality associated with displaying a list of all Dcotors to the user.
 * Uses the layout as specified in res/layout/fragment_doctor.xml.
 */
public class DoctorFragment extends Fragment {

  private DoctorViewModel viewModel;
  private FragmentDoctorBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  /**
   * Overrides the onCreateView method in Fragment.  Instantiates local variables.
   * Inflates (sets up and displays) the layout as specified in fragment_doctor.xml.
   * @param savedInstanceState a {@link Bundle}.
   * @param container a {@link ViewGroup}.
   * @param inflater a {@link LayoutInflater}.
   */
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentDoctorBinding.inflate(inflater, container, false);
    //binding.submit.setOnClickListener((v) ->
    //    viewModel.submitGuess(binding.guess.getText().toString().trim()));
    //binding.guess.setFilters(new InputFilter[]{this});
    //compiler infers that v is a view : (View v)
    binding.addDoctor.setOnClickListener(
        v -> editDoctor(0, v));
    return binding.getRoot();
  }

  /**
   * Overrides the onViewCreated method in Fragment.  Specifically, interacts with the
   * {@link DoctorViewModel} to get a list of doctors from the database.
   * @param view a {@link View}.
   * @param savedInstanceState a {@link Bundle}.
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
//    viewModel = new ViewModelProvider(getActivity()).get(DoctorViewModel.class);
//    getLifecycle().addObserver(viewModel);
//    viewModel.getThrowable().observe(getViewLifecycleOwner(), this::displayError);
//    viewModel.getVaccines().observe(getViewLifecycleOwner(), this::update); //observes a vaccine
    viewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
    viewModel
        .getDoctors()
        .observe(getViewLifecycleOwner(), (doctors) -> {
          DoctorAdapter adapter = new DoctorAdapter(getContext(), doctors, this::editDoctor,
              (doctor, v) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                        viewModel.deleteDoctor(doctor);
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
          binding.doctors.setAdapter(adapter);
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

//  private void update(Doctor doctor) { //updates the display model
//    DoctorAdapter adapter = new DoctorAdapter(getContext(),
//        doctor.getName());// how we get a context in a fragment
//    binding.doctor.setAdapter(adapter);//this adapter can tell us our guesses
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

  private void editDoctor(long id, View view) {
    Navigation.findNavController(binding.getRoot())
        .navigate(DoctorFragmentDirections.openDoctor().setDoctorId(id));
  }
}