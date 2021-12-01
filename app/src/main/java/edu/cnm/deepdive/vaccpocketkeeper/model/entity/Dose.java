package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Encapsulates a persistent Dose object with: id, created, vaccineId, doctorId, name, dateAdministered, and image.
 */
@Entity(tableName = "dose",
    foreignKeys = {
        @ForeignKey(
            entity = Vaccine.class,
            parentColumns = "vaccine_id",
            childColumns = "vaccine_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Doctor.class,
            parentColumns = "doctor_id",
            childColumns = "doctor_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Dose {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "dose_id") //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @ColumnInfo(name = "vaccine_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affinity; pimarykey is automatically indexed and automatically unique
  private long vaccineId;

  @ColumnInfo(name = "doctor_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affinity; pimarykey is automatically indexed and automatically unique
  private Long doctorId;

  @NonNull
  @ColumnInfo
  private String name = "";

  @ColumnInfo(name = "date_administered")
  private Date dateAdministered;

  private String image;

  /**
   * Returns the local id field.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the local id field to the id parameter
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the local created {@link Date} field
   */
  @NonNull
  public Date getCreated() {
    return created;
  }

  /**
   * Set the local created {@link Date} field to the created {@link Date} argument.
   */
  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  /**
   * Returns the local vaccineId field.
   */
  public long getVaccineId() {
    return vaccineId;
  }

  /**
   * Sets the local vaccineId field to the vaccineId parameter.
   */
  public void setVaccineId(long vaccineId) {
    this.vaccineId = vaccineId;
  }

  /**
   * Returns the local doctorId field.
   */
  public Long getDoctorId() {
    return doctorId;
  }

  /**
   * Sets the local doctorId field to the doctorId parameter.
   */
  public void setDoctorId(Long doctorId) {
    this.doctorId = doctorId;
  }

  /**
   * Returns the local name field.
   */
  @NonNull
  public String getName() {
    return name;
  }

  /**
   * Sets the local field name to the name parameter provided.
   */
  public void setName(@NonNull String name) {
    this.name = name;
  }

  /**
   * Returns the local created {@link Date} dateAdministered field
   */
  public Date getDateAdministered() {
    return dateAdministered;
  }

  /**
   * Set the local created {@link Date} dateAdministered field to the created {@link Date} dateAdministered argument.
   */
  public void setDateAdministered(Date dateAdministered) {
    this.dateAdministered = dateAdministered;
  }

  /**
   * Returns the local image field.
   */
  public String getImage() {
    return image;
  }

  /**
   * Sets the local field image to the image parameter provided.
   */
  public void setImage(String image) {
    this.image = image;
  }
}
