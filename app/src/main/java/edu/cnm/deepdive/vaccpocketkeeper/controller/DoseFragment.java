package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import edu.cnm.deepdive.vaccpocketkeeper.adapter.DoseAdapter;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoseViewModel;

public class DoseFragment extends Fragment {

  private DoseViewModel viewModel;
  private FragmentDoseBinding binding;
  private Vaccine vaccine;

  //TODO: adding doses should increase the total number of doses in the vaccine by 1.

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
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
    viewModel = new ViewModelProvider(this).get(DoseViewModel.class);
    viewModel
        .getDosesForVaccineId(1)//TODO: get vaccineId from vaccine.
        .observe(getViewLifecycleOwner(),(doses) -> {
          DoseAdapter adapter = new DoseAdapter(getContext(), doses, this::editDose,
              (dose,v) -> viewModel.deleteDose(dose));//TODO: show alert confirming deletion to user (have delete dose method to confirm and say to delete)
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

  public void editDose(long id, View view) {
    Navigation.findNavController(binding.getRoot())
        .navigate(DoseFragmentDirections.openDose().setDoseId(id));
  }

}