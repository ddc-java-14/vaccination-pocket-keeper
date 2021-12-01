package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.List;

/**
 * Encapsulates a persistent UserWithVaccine object that is a join of {@link User} and {@link Vaccine} by user_id.
 */
public class UserWithVaccines extends User {

  @Relation(
      entity = Vaccine.class,
      entityColumn = "user_id",
      parentColumn = "user_id"
  )
  private List<Vaccine> vaccines;

  /**
   * Returns the local vaccines field.
   */
  public List<Vaccine> getVaccines() {
    return vaccines;
  }

  /**
   * Sets the local field vaccine to the {@link List} of {@link Vaccine} objects parameter provided.
   */
  public void setVaccines(List<Vaccine> vaccines) {
    this.vaccines = vaccines;
  }
}
