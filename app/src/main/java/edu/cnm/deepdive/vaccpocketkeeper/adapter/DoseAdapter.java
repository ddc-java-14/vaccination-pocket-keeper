package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemDoseBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import java.text.DateFormat;
import java.util.List;

public class DoseAdapter extends RecyclerView.Adapter<DoseAdapter.Holder> {
  //TODO: change fragment_edit_dose layouts to doctor spinner dropdown and datepicker widgets
  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final VaccineWithDoses vaccine;
  private final List<Dose> doses;
  private final OnDoseEditHelper onDoseEditHelper;
  private final OnDoseDeleteHelper onDoseDeleteHelper;


  public DoseAdapter(Context context, VaccineWithDoses vaccine,
      OnDoseEditHelper onDoseEditHelper,
      OnDoseDeleteHelper onDoseDeleteHelper) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.onDoseEditHelper = onDoseEditHelper;
    this.onDoseDeleteHelper = onDoseDeleteHelper;
    this.vaccine = vaccine;
    this.doses = vaccine.getDoses();
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

  class Holder extends RecyclerView.ViewHolder {

    private final ItemDoseBinding binding;

    private Holder(@NonNull ItemDoseBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Dose dose = doses.get(position);
      binding.doseName.setText("Doses for Vaccine: " + vaccine.getName());//TODO get this from nav argument/database
      //binding.doseDoctor.setText(dose.getDoctorId().toString());//TODO retrieve this from database
      binding.dateAdministered.setText(String.valueOf(dose.getDateAdministered()));//TODO double check this line doesn't cause the app to crash
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