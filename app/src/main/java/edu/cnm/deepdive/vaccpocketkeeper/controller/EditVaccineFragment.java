package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentEditVaccineBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.VaccineViewModel;

public class EditVaccineFragment extends BottomSheetDialogFragment implements TextWatcher {

  private FragmentEditVaccineBinding binding;
  private VaccineViewModel viewModel;
  private long vaccineId;
  private VaccineWithDoses vaccine;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EditVaccineFragmentArgs args = EditVaccineFragmentArgs.fromBundle(getArguments()); //generated for us by navigation framework
    vaccineId = args.getVaccineId(); //autogenerated getVaccineId from navgraph argument name
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentEditVaccineBinding.inflate(inflater, container, false);
    binding.vaccVaccineName.addTextChangedListener(this);
    binding.vaccDescription.addTextChangedListener(this);
    binding.vaccFrequency.addTextChangedListener(this);
    binding.vaccTotalNumberOfDoses.addTextChangedListener(this);
    binding.vaccAgeRangeLowerLimit.addTextChangedListener(this);
    binding.vaccAgeRangeUpperLimit.addTextChangedListener(this);
    binding.cancel.setOnClickListener((v) -> dismiss());
    binding.save.setOnClickListener((v) -> {
      vaccine.setName(binding.vaccVaccineName.getText().toString().trim());
      vaccine.setDescription(binding.vaccDescription.getText().toString().trim());
      vaccine.setTotalNumberOfDoses(Integer.parseInt(binding.vaccTotalNumberOfDoses.getText().toString().trim()));
      vaccine.setFrequency(Integer.parseInt(binding.vaccFrequency.getText().toString().trim()));
      vaccine.setAgeRangeLowerLimit(Integer.parseInt(binding.vaccAgeRangeLowerLimit.getText().toString().trim()));
      vaccine.setAgeRangeUpperLimit(Integer.parseInt(binding.vaccAgeRangeUpperLimit.getText().toString().trim()));
      viewModel.save(vaccine);
      dismiss();
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(VaccineViewModel.class);
    if (vaccineId != 0) { //previous vaccine
      viewModel.setVaccineId(vaccineId);
      viewModel.getVaccine().observe(getViewLifecycleOwner(), (vaccine) -> {
        this.vaccine = vaccine;
        binding.vaccVaccineName.setText(vaccine.getName());
        binding.vaccDescription.setText(vaccine.getDescription());
        //binding.vaccFrequency.setEnabled(false);
        binding.vaccFrequencyLayout.setVisibility(View.GONE);
            //.setText(String.valueOf(vaccine.getFrequency()));
        //binding.vaccTotalNumberOfDoses.setEnabled(false);
        binding.vaccTotalNumberOfDosesLayout.setVisibility(View.GONE);
        //.setText(String.valueOf(vaccine.getTotalNumberOfDoses()));
        binding.vaccAgeRangeLowerLimit.setText(String.valueOf(vaccine.getAgeRangeLowerLimit()));
        binding.vaccAgeRangeUpperLimit.setText(String.valueOf(vaccine.getAgeRangeUpperLimit()));
      });
    } else { //new vaccine
      vaccine = new VaccineWithDoses();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    //Do nothing.
  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    //Do nothing.
  }

  @Override
  public void afterTextChanged(Editable editable) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    String vaccine = binding.vaccVaccineName.getText().toString().trim();
    String description = binding.vaccDescription.getText().toString().trim();
    String frequency = binding.vaccFrequency.getText().toString().trim();
    String totalNumberOfDoses = binding.vaccTotalNumberOfDoses.getText().toString().trim();
    String ageRangeLowerLimt = binding.vaccAgeRangeLowerLimit.getText().toString().trim();
    String ageRangeUpperLimit = binding.vaccAgeRangeUpperLimit.getText().toString().trim();
    //Log.d(getClass().getSimpleName(), String.format("vaccine is empty: %b, description is empty: %b, frequency is empty: %b, total number of doses is empty: %b, age range lower limit is empty: %b, age range upper limit is empty: %b%n", vaccine.isEmpty(), description.isEmpty(), frequency.isEmpty(), totalNumberOfDoses.isEmpty(), ageRangeLowerLimt.isEmpty(), ageRangeUpperLimit.isEmpty()));
    binding.save.setEnabled(!vaccine.isEmpty() && !description.isEmpty() && !frequency.isEmpty() && !totalNumberOfDoses.isEmpty() && !ageRangeLowerLimt.isEmpty() && !ageRangeUpperLimit.isEmpty());
  }
}
