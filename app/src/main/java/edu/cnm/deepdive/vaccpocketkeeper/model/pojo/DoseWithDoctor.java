package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;

/**
 * Encapsulates a persistent DosesWithDoctor object that is a join of {@link Doctor} and {@link Dose} by doctor_id.
 */
public class DoseWithDoctor extends Dose {

  @Relation(
      entity = Doctor.class,
      parentColumn = "doctor_id",
      entityColumn = "doctor_id"
  )
  private Doctor doctor;

  /**
   * Returns the local doctor field.
   */
  public Doctor getDoctor() {
    return doctor;
  }

  /**
   * Sets the local field doctor to the {@link Doctor} object parameter provided.
   */
  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }
}
