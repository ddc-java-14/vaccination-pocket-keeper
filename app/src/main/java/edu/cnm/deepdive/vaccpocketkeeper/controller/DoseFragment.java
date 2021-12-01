package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.adapter.DoseAdapter;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoseViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.VaccineViewModel;
import java.util.List;

/**
 * Implements the layout and functionality associated with displaying a list of all Doses to the user.
 * Uses the layout as specified in res/layout/fragment_dose.xml.
 */
public class DoseFragment extends Fragment {

  private DoseViewModel doseViewModel;
  private VaccineViewModel vaccineViewModel;
  private DoctorViewModel doctorViewModel;
  private FragmentDoseBinding binding;
  private long vaccineId;
  private VaccineWithDoses vaccine;
  private Doctor doctor;
  private List<Doctor> doctors;

  /**
   * Overrides the onCreate method in Fragment.  Instantiates local variables.  Gets the vaccineId to display the
   * Doses associated with that particular vaccine.
   * @param savedInstanceState
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    DoseFragmentArgs args = DoseFragmentArgs.fromBundle(getArguments());
    vaccineId = args.getVaccineId();
    Log.d(getClass().getSimpleName(),"VaccineId (inside DoseFragment): "+vaccineId);
  }

  /**
   * Overrides the onCreateView method in Fragment. Inflates (sets up and displays) the layout as
   * specified in fragment_dose.xml.
   * @param inflater a {@link LayoutInflater}.
   * @param container a {@link ViewGroup}.
   * @param savedInstanceState a {@link Bundle}.
   * @return a {@link View}.
   */
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentDoseBinding.inflate(inflater, container, false);
    //binding.submit.setOnClickListener((v) ->
    //    viewModel.submitGuess(binding.guess.getText().toString().trim()));
    //binding.guess.setFilters(new InputFilter[]{this});
    //compiler infers that v is a view : (View v)
    binding.addDose.setOnClickListener(
        v -> editDose(0,vaccineId, v));
    return binding.getRoot();
  }

  /**
   * Overrides the onViewCreated method in Fragment.  Specifically, interacts with the
   * {@link VaccineViewModel} and {@link DoctorViewModel} to get a list of vaccines and doctors from the
   * database.
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
    vaccineViewModel = new ViewModelProvider(this).get(VaccineViewModel.class);
    vaccineViewModel
        .getVaccine()
        .observe(getViewLifecycleOwner(), (vaccine) -> {
          binding.doseVaccineName.setText("Doses for " + vaccine.getName());
        });
    doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
    doctorViewModel
        .getDoctors()
        //.getDoctorById(2)
        .observe(getViewLifecycleOwner(), (doctors) -> {
      //this.doctor = doctor;
        this.doctors = doctors;});
    vaccineViewModel
        .getVaccine()
        .observe(getViewLifecycleOwner(), (vaccine) -> {
          DoseAdapter adapter = new DoseAdapter(getContext(), vaccine,
              (doseId, view1) -> editDose(doseId, vaccineId, view1),
              (dose,v) -> doseViewModel.deleteDose(dose));//TODO: show alert confirming deletion to user (have delete dose method to confirm and say to delete)
          binding.doses.setAdapter(adapter);
        });
    vaccineViewModel.setVaccineId(vaccineId);
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

  private void editDose(long doseId, long vaccineId, View view) {
    Navigation.findNavController(binding.getRoot())
        .navigate(DoseFragmentDirections.openDose()
            .setVaccineId(vaccineId)
            .setDoseId(doseId));
  }

}