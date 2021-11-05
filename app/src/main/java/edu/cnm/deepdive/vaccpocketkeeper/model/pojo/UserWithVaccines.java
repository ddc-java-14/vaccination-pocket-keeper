package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.List;

public class UserWithVaccines extends User {

  @Relation(
      entity = Vaccine.class,
      entityColumn = "user_id",
      parentColumn = "user_id"
  )
  private List<Vaccine> vaccines;

  public List<Vaccine> getVaccines() {
    return vaccines;
  }

  public void setVaccines(List<Vaccine> vaccines) {
    this.vaccines = vaccines;
  }
}
