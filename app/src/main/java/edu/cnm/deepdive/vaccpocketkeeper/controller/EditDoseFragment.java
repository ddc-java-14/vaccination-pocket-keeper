package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentEditDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoseViewModel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditDoseFragment extends BottomSheetDialogFragment implements TextWatcher {

  private FragmentEditDoseBinding binding;
  private DoseViewModel doseViewModel;
  private DoctorViewModel doctorViewModel;
  private long doseId;
  private Dose dose;
  //private final DateFormat dateFormat;
  private Spinner doctorSelector;
  private ArrayAdapter<Doctor> adapter;
  private Doctor doctor;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EditDoseFragmentArgs args = EditDoseFragmentArgs.fromBundle(
        getArguments()); //generated for us by navigation framework
    doseId = args.getDoseId(); //autogenerated getDoseId from navgraph argument name
    //dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
    doctor = null;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Doctor doctor;
    binding = FragmentEditDoseBinding.inflate(inflater, container, false);
    binding.doseName.addTextChangedListener(this);
    binding.doctorNameSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        EditDoseFragment.this.doctor = (Doctor) parent.getItemAtPosition(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        EditDoseFragment.this.doctor = null;
      }
    });
    binding.dateAdministered.setOnDateChangedListener(new OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        Calendar selected = new GregorianCalendar(year, month, day);
        //selected.getTime();
      }
    });
    binding.cancel.setOnClickListener((v) -> dismiss());
    binding.save.setOnClickListener((v) -> {
      //TODO: make this into a date picker
      //format: LocalDate.parse("2018-05-05");
      //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
      Calendar calendar = Calendar.getInstance();
      try {
        calendar.set(Calendar.MONTH, binding.dateAdministered.getMonth());
        calendar.set(Calendar.YEAR, binding.dateAdministered.getYear());
        calendar.set(Calendar.DAY_OF_WEEK, binding.dateAdministered.getDayOfMonth());
        Date dt = calendar.getTime();
        dose.setDateAdministered(dt);
      } catch (Exception e) {
        e.printStackTrace();
        Log.e(getClass().getSimpleName(), e.getMessage());
        // "There was an error parsing the date from a user string.");
      }
      if (this.doctor != null) {
        dose.setDoctorId(this.doctor.getId());
      }
      dose.setName(binding.doseName.getText().toString().trim());
      doseViewModel.save(dose);
      dismiss();
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    doseViewModel = new ViewModelProvider(this).get(DoseViewModel.class);
    if (doseId != 0) { //previous dose
      doseViewModel.setDoseId(doseId);
      //binding.doctorNameSelector.setSelection(binding.doctorNameSelector.getSelectedItem(dose.getName());
      doseViewModel.getDose().observe(getViewLifecycleOwner(), (dose) -> {
        this.dose = dose;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, dose.getDateAdministered().getMonth());
        calendar.set(Calendar.YEAR, dose.getDateAdministered().getYear());
        calendar.set(Calendar.DAY_OF_WEEK, dose.getDateAdministered().getDay());
        //calendar.setTime(dose.getDateAdministered());
            //dose.getDateAdministered().getYear(), dose.getDateAdministered().getMonth(), dose.getDateAdministered().getDay());
        binding.dateAdministered.updateDate(dose.getDateAdministered().getYear(), dose.getDateAdministered().getMonth(), dose.getDateAdministered().getDay());
        if (doctor != null) {
          int selectedPosition = adapter.getPosition(doctor);
          //String selectedItem = adapter.getItemAtPosition(selectedPosition).toString();
          binding.doctorNameSelector.setSelection(selectedPosition);
        }
        binding.doseName.setText(dose.getName());
      });
    } else { //new dose
      dose = new Dose();
    }
    doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
    doctorViewModel
        .getDoctors()
        .observe(getViewLifecycleOwner(), (doctors) -> {
          if (doctors != null) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                doctors);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.doctorNameSelector.setAdapter(adapter);
          }
        });
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
    String dose = binding.doseName.getText().toString().trim();
    binding.save.setEnabled(!dose.isEmpty());
  }

}
