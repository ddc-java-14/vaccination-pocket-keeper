package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoctorDialogueBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;

/**
 * Implements the layout and functionality associated with displaying a list of all Dcotors to the user.
 * Uses the layout as specified in res/layout/fragment_doctor_dialogue.xml.
 */
public class DoctorDialogueFragment extends DialogFragment {

  private FragmentDoctorDialogueBinding binding;
  private DoctorViewModel viewModel;
  private Doctor doctor;
  private long doctorId;

  /**
   * Overrides the onCreate method in Fragment.  Gets the doctorId to display the
   * Doctor associated with that particular id..
   * @param savedInstanceState
   */
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      DoctorDialogueFragmentArgs args = DoctorDialogueFragmentArgs.fromBundle(getArguments());
      doctorId = args.getDoctorId();
    }
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentDoctorDialogueBinding.inflate(LayoutInflater.from(getContext()));
    return new AlertDialog.Builder(
        getContext())
        .setTitle((doctor != null) ? doctor.getName() : "No Name Available")
        .setView(binding.getRoot())
        .setPositiveButton("close", (dlg, which) -> {
        })
        .create();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(DoctorViewModel.class);
  }
}
