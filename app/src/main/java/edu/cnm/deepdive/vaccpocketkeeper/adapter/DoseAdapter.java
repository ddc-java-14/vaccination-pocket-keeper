package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.service.DoctorRepository;
import edu.cnm.deepdive.vaccpocketkeeper.service.DoseRepository;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoctorViewModel;
import java.text.DateFormat;
import java.util.List;

public class DoseAdapter extends RecyclerView.Adapter<DoseAdapter.Holder> {
  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final VaccineWithDoses vaccine;
  private final List<Dose> doses;
  //private final Doctor doctor;
  private final OnDoseEditHelper onDoseEditHelper;
  private final OnDoseDeleteHelper onDoseDeleteHelper;
  private DoctorViewModel doctorViewModel;

  public DoseAdapter(Context context, VaccineWithDoses vaccine,
      OnDoseEditHelper onDoseEditHelper,
      OnDoseDeleteHelper onDoseDeleteHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onDoseEditHelper = onDoseEditHelper;
    this.onDoseDeleteHelper = onDoseDeleteHelper;
    this.vaccine = vaccine;
    this.doses = vaccine.getDoses();
    //this.doctor = doctor;
    /*doses.forEach((dose)-> {dose.getDoctorId();
        doctorViewModel.getDoctor().observe(getViewLifecycleOwner(), (doctor) -> {
        this.doctor = doctor;}););*/
    //lifecycle = new LifecycleRegistry(this);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemDoseBinding.inflate(inflator, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return doses.size();
  }

//  @NonNull
//  @Override
//  public Lifecycle getLifecycle() {
//    return lifecycle;
//  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemDoseBinding binding;

    private Holder(@NonNull ItemDoseBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Log.d(getClass().getSimpleName(), "value: "+value);
      //Use contents of model object to set contents of binding fields.
      Dose dose = doses.get(position);
      binding.doseName.setText(dose.getName());
      if (dose.getDoctorId() != null) {
          //doctorViewModel.getDoctor().observe(getViewLifecycleOwner(), (doctor) -> {
          //this.doctor = doctor;});
        binding.doseDoctor.setText(dose.getDoctorId().toString());
      }
      binding.dateAdministered.setText(String.valueOf(dose.getDateAdministered()));
      binding.doseEdit.setOnClickListener(
          (v) -> onDoseEditHelper.onDoseClick(dose.getId(), v));
      binding.doseDelete.setOnClickListener(
          (v) -> onDoseDeleteHelper.onDoseClick(dose, v));
    }
  }

  public interface OnDoseEditHelper {
    void onDoseClick(long id, View view);
  }

  public interface OnDoseDeleteHelper {
    void onDoseClick(Dose dose, View view);
  }
}