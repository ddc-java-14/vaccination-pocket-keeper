package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
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

public class DoseFragment extends Fragment {

  private DoseViewModel doseViewModel;
  private VaccineViewModel vaccineViewModel;
  private DoctorViewModel doctorViewModel;
  private FragmentDoseBinding binding;
  private long vaccineId;
  private VaccineWithDoses vaccine;
  private Doctor doctor;
  private List<Doctor> doctors;

  //TODO: adding doses should increase the total number of doses in the vaccine by 1.

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    DoseFragmentArgs args = DoseFragmentArgs.fromBundle(getArguments());
    vaccineId = args.getVaccineId();
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentDoseBinding.inflate(inflater, container, false);
    //binding.submit.setOnClickListener((v) ->
    //    viewModel.submitGuess(binding.guess.getText().toString().trim()));
    //binding.guess.setFilters(new InputFilter[]{this});
    //compiler infers that v is a view : (View v)
    binding.addDose.setOnClickListener(
        v -> editDose(0,v));
    return binding.getRoot();
  }

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
          DoseAdapter adapter = new DoseAdapter(getContext(), vaccine, doctors,
              (id, view1) -> editDose(id, view1),
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

  public void editDose(long id, View view) {
    Navigation.findNavController(binding.getRoot())
        .navigate(DoseFragmentDirections.openDose().setDoseId(id));
  }

}