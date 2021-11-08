package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

  public DoctorAdapter(Context context, List<Doctor> doctors) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.doctors = doctors;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemDoctorBinding.inflate(inflator, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return doctors.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemDoctorBinding binding;

    private Holder(@NonNull ItemDoctorBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Doctor doctor = doctors.get(position);
      binding.doctorName.setText(doctor.getName());
    }
  }
}