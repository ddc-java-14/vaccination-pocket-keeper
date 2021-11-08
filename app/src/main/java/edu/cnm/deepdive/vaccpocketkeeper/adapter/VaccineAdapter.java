package edu.cnm.deepdive.vaccpocketkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ItemVaccineBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.text.DateFormat;
import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.Holder> {

  private final LayoutInflater inflator;
  private final DateFormat dateFormat;
  private final List<Vaccine> vaccines;

  public VaccineAdapter(Context context, List<Vaccine> vaccines) {
    inflator = LayoutInflater.from(context);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    this.vaccines = vaccines;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemVaccineBinding.inflate(inflator, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return vaccines.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemVaccineBinding binding;

    private Holder(@NonNull ItemVaccineBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      //Use contents of model object to set contents of binding fields.
      Vaccine vaccine = vaccines.get(position);
      binding.vaccineName.setText(vaccine.getName());
      //binding..setText(vaccine.getName());
    }
  }
}