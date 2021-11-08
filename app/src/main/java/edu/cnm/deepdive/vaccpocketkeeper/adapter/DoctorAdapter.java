package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemDoctorBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import java.text.DateFormat;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {

  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<Doctor> doctors;
  private final OnDoctorClickHelper onDoctorClickHelper;

  public DoctorAdapter(Context context, List<Doctor> doctors, OnDoctorClickHelper onDoctorClickHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onDoctorClickHelper = onDoctorClickHelper;
    this.doctors = doctors;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemDoctorBinding binding = ItemDoctorBinding.inflate(inflator, parent, false);
    return new Holder(binding, onDoctorClickHelper);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return doctors.size();
  }

  class Holder extends RecyclerView.ViewHolder implements OnClickListener {

    private final ItemDoctorBinding binding;
    final OnDoctorClickHelper onDoctorClickHelper;

    private Holder(@NonNull ItemDoctorBinding binding, OnDoctorClickHelper onDoctorClickHelper) {
      super(binding.getRoot());
      this.onDoctorClickHelper = onDoctorClickHelper;
      this.binding = binding;
      binding.getRoot().setOnClickListener(this);
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Doctor doctor = doctors.get(position);
      binding.doctorName.setText(doctor.getName());
      binding.getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      onDoctorClickHelper.onDoctorClick(doctors.get(getBindingAdapterPosition()).getId(),view);
    }
  }

  public interface OnDoctorClickHelper {
    void onDoctorClick(long id, View view);
  }
}