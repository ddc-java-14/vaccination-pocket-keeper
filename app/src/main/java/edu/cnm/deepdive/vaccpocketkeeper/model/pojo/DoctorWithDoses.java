package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.List;

public class DoctorWithDoses extends Doctor {

  @Relation(
      entity = Dose.class,
      entityColumn = "dose_id",
      parentColumn = "dose_id"
  )
  private List<Dose> doses;

  @Override
  public List<Dose> getDoses() {
    return doses;
  }

  public void setDoses(List<Dose> doses) {
    this.doses = doses;
  }
}
