package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import java.text.DateFormat;
import java.util.List;

/**
 * Populates all future {@link Dose}s of all {@link Vaccine}s into a {@link RecyclerView} as specified by the accompanying item layout.
 */
public class AllFutureDosesAdapter extends RecyclerView.Adapter<AllFutureDosesAdapter.Holder> {
  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<DoseWithDoctor> doses;
  private final OnDoseEditHelper onDoseEditHelper;
  private final OnDoseDeleteHelper onDoseDeleteHelper;

  /**
   * Class constructor.  Initializes local variables.
   * @param context the context.
   * @param doses a list of {@link Dose}s to be populated into the {@link RecyclerView}
   * @param onDoseEditHelper a helper class object to aid in editing a {@link Dose} object.
   * @param onDoseDeleteHelper a helper class object to aid in deleting a {@link Doctor} object.
   */
  public AllFutureDosesAdapter(Context context, List<DoseWithDoctor> doses,
      OnDoseEditHelper onDoseEditHelper,
      OnDoseDeleteHelper onDoseDeleteHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onDoseEditHelper = onDoseEditHelper;
    this.onDoseDeleteHelper = onDoseDeleteHelper;
    this.doses = doses;
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

  /**
   * A helper class that binds each {@link Dose} to a particular position in the {@link RecyclerView}.
   */
  class Holder extends RecyclerView.ViewHolder {

    private final ItemDoseBinding binding;

    private Holder(@NonNull ItemDoseBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      DoseWithDoctor dose = doses.get(position);
      binding.doseName.setText(dose.getName());
      if (dose.getDoctorId() != null) {
        binding.doseDoctor.setText(dose.getDoctor().getName());
      }
      binding.dateAdministered.setText(String.valueOf(dose.getDateAdministered()));
      binding.doseEdit.setOnClickListener(
          (v) -> onDoseEditHelper.onDoseClick(dose.getId(), v));
      binding.doseDelete.setOnClickListener(
          (v) -> onDoseDeleteHelper.onDoseClick(dose, v));
    }
  }

  /**
   * A helper class to aid in editing a {@link Dose} object.
   */
  public interface OnDoseEditHelper {
    void onDoseClick(long id, View view);
  }

  /**
   * A helper class to aid in deleting a {@link Dose} object.
   */
  public interface OnDoseDeleteHelper {
    void onDoseClick(Dose dose, View view);
  }
}