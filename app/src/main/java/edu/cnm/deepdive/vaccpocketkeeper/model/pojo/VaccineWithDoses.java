package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates a persistent VaccineWithDoses object that is a join of {@link Vaccine} and {@link Dose} by vaccine_id.
 */
public class VaccineWithDoses extends Vaccine {

  @Relation(
      entity = Dose.class,
      entityColumn = "vaccine_id",
      parentColumn = "vaccine_id"
  )
  private List<DoseWithDoctor> doses = new LinkedList<>();

  /**
   * Returns the local doses field.
   */
  public List<DoseWithDoctor> getDoses() {
    return doses;
  }

  /**
   * Sets the local field doses to the {@link List} of {@link Dose} objects parameter provided.
   */
  public void setDoses(List<DoseWithDoctor> doses) {
    this.doses = doses;
  }
}
