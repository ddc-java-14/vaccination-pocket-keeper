package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    vaccineViewModel = new ViewModelProvider(this).get(VaccineViewModel.class);
    vaccineViewModel
        .getVaccine()
        .observe(getViewLifecycleOwner(), (vaccine) -> {
          binding.doseVaccineName.setText("Doses for " + vaccine.getName());
        });
    doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
    doctorViewModel
        .getDoctors()
        .observe(getViewLifecycleOwner(), (doctors) -> {
        this.doctors = doctors;});
    vaccineViewModel
        .getVaccine()
        .observe(getViewLifecycleOwner(), (vaccine) -> {
          DoseAdapter adapter = new DoseAdapter(getContext(), vaccine,
              (doseId, view1) -> editDose(doseId, vaccineId, view1),
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
    vaccineViewModel.setVaccineId(vaccineId);
  } //when fragment dies, then cleans up

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }


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