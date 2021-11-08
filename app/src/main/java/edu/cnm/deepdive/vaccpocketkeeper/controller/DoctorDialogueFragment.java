package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentDoctorDialogueBinding;


public class DoctorDialogueFragment extends DialogFragment {

  private FragmentDoctorDialogueBinding binding;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentDoctorDialogueBinding.inflate(LayoutInflater.from(getContext()));
    binding.doctorDialogueName.getText().toString();
    return new AlertDialog.Builder(
        getContext())
        .setTitle("Doctor Name")
        .setView(binding.getRoot())
        .setPositiveButton("close",(dlg,which) -> {})
        .create();
  }
}
