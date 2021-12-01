package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemVaccineBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.text.DateFormat;
import java.util.List;

/**
 * Populates {@link Vaccine}s into a {@link RecyclerView} as specified by the accompanying item layout.
 */
public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.Holder> {

  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<Vaccine> vaccines;
  private final OnVaccineEditHelper onVaccineEditHelper;
  private final OnVaccineDeleteHelper onVaccineDeleteHelper;
  private final OnVaccineClickHelper onVaccineClickHelper;

  /**
   * Class constructor.  Initializes local variables.
   * @param context the context.
   * @param vaccines a list of {@link Vaccine}s to be populated into the {@link RecyclerView}
   * @param onVaccineEditHelper a helper class object to aid in editing a {@link Vaccine} object.
   * @param onVaccineDeleteHelper a helper class object to aid in deleting a {@link Vaccine} object.
   * @param onVaccineClickHelper a helper class object to aid in going to a particular {@link Vaccine} details object, which specifies all of the Doses of a {@link Vaccine}.
   */
  public VaccineAdapter(Context context, List<Vaccine> vaccines,
      OnVaccineEditHelper onVaccineEditHelper,
      OnVaccineDeleteHelper onVaccineDeleteHelper, OnVaccineClickHelper onVaccineClickHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onVaccineEditHelper = onVaccineEditHelper;
    this.onVaccineDeleteHelper = onVaccineDeleteHelper;
    this.onVaccineClickHelper = onVaccineClickHelper;
    this.vaccines = vaccines;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemVaccineBinding binding = ItemVaccineBinding.inflate(inflator, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return vaccines.size();
  }

  /**
   * A helper class that binds each {@link Vaccine} to a particular position in the {@link RecyclerView}.
   */
  class Holder extends RecyclerView.ViewHolder {

    private final ItemVaccineBinding binding;
    private long testId;

    private Holder(@NonNull ItemVaccineBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Vaccine vaccine = vaccines.get(position);
      binding.vaccineName.setText(vaccine.getName());
      binding.description.setText(vaccine.getDescription());
      binding.frequency.setText(String.valueOf(vaccine.getFrequency()));
      binding.totalNumberOfDoses.setText(String.valueOf(vaccine.getTotalNumberOfDoses()));
      binding.ageRangeLowerLimit.setText(String.valueOf(vaccine.getAgeRangeLowerLimit()));
      binding.ageRangeUpperLimit.setText(String.valueOf(vaccine.getAgeRangeUpperLimit()));
      binding.vaccEdit.setOnClickListener(
          (v) -> onVaccineEditHelper.onVaccineClick(vaccine.getId(), v));
      binding.vaccDelete.setOnClickListener(
          (v) -> onVaccineDeleteHelper.onVaccineClick(vaccine, v));
      binding.getRoot()
          .setOnClickListener((view) -> onVaccineClickHelper.onVaccineClick(vaccine.getId(), view));
    }
  }

  /**
   * A helper class object to aid in going to a particular {@link Vaccine} details object,
   * which specifies all of the Doses of a {@link Vaccine}.
   */
  public interface OnVaccineClickHelper {
    void onVaccineClick(long id, View view);
  }

  /**
   * A helper class to aid in editing a {@link Vaccine} object.
   */
  public interface OnVaccineEditHelper {
    void onVaccineClick(long id, View view);
  }

  /**
   * A helper class to aid in deleting a {@link Vaccine} object.
   */
  public interface OnVaccineDeleteHelper {
    void onVaccineClick(Vaccine vaccine, View view);
  }
}