package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.LinkedList;
import java.util.List;

public class VaccineWithDoses extends Vaccine {

  @Relation(
      entity = Dose.class,
      entityColumn = "vaccine_id",
      parentColumn = "vaccine_id"
  )
  private List<DoseWithDoctor> doses = new LinkedList<>();

  public List<DoseWithDoctor> getDoses() {
    return doses;
  }

  public void setDoses(List<DoseWithDoctor> doses) {
    this.doses = doses;
  }
}
