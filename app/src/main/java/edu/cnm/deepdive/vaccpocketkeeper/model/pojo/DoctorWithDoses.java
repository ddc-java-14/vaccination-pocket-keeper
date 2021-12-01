package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.List;

/**
 * Encapsulates a persistent DoctorWithDoses object that is a join of {@link Doctor} and {@link Dose} by dose_id.
 */
public class DoctorWithDoses extends Doctor {

  @Relation(
      entity = Dose.class,
      entityColumn = "dose_id",
      parentColumn = "dose_id"
  )
  private List<Dose> doses;

  /**
   * Returns the local doses field.
   */
  public List<Dose> getDoses() {
    return doses;
  }

  /**
   * Sets the local field doses to the {@link List} of {@link Dose} objects parameter provided.
   */
  public void setDoses(List<Dose> doses) {
    this.doses = doses;
  }
}
