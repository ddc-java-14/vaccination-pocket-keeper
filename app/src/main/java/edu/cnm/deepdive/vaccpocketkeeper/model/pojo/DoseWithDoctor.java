package edu.cnm.deepdive.vaccpocketkeeper.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;

public class DoseWithDoctor extends Dose {

  @Relation(entity = Doctor.class, parentColumn = "doctor_id", entityColumn = "doctor_id")
  private Doctor doctor;

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }
}
