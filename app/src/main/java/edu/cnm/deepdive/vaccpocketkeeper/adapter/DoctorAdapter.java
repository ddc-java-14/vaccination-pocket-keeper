package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemDoctorBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import java.text.DateFormat;
import java.util.List;

/**
 * Populates {@link Doctor}s into a {@link RecyclerView} as specified by the accompanying item layout.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {

  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<Doctor> doctors;
  private final OnDoctorEditHelper onDoctorEditHelper;
  private final OnDoctorDeleteHelper onDoctorDeleteHelper;

  /**
   * Class constructor.  Initializes local variables.
   * @param context the context.
   * @param doctors a list of {@link Doctor}s to be populated into the {@link RecyclerView}
   * @param onDoctorEditHelper a helper class object to aid in editing a {@link Doctor} object.
   * @param onDoctorDeleteHelper a helper class object to aid in deleting a {@link Doctor} object.
   */
  public DoctorAdapter(Context context, List<Doctor> doctors, OnDoctorEditHelper onDoctorEditHelper,
      OnDoctorDeleteHelper onDoctorDeleteHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onDoctorEditHelper = onDoctorEditHelper;
    this.onDoctorDeleteHelper = onDoctorDeleteHelper;
    this.doctors = doctors;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemDoctorBinding binding = ItemDoctorBinding.inflate(inflator, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return doctors.size();
  }

  /**
   * A helper class that binds each {@link Doctor} to a particular position in the {@link RecyclerView}.
   */
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
      binding.edit.setOnClickListener((v) -> onDoctorEditHelper.onDoctorClick(doctor.getId(),v));
      binding.delete.setOnClickListener((v) -> onDoctorDeleteHelper.onDoctorClick(doctor,v));
    }

  }

  /**
   * A helper class to aid in editing a {@link Doctor} object.
   */
  public interface OnDoctorEditHelper {
    void onDoctorClick(long id, View view);
  }

  /**
   * A helper class to aid in deleting a {@link Doctor} object.
   */
  public interface OnDoctorDeleteHelper {
    void onDoctorClick(Doctor doctor, View view);
  }
}