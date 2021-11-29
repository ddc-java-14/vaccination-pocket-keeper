package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

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

  @ColumnInfo(index = true)
  private boolean administered = false;

  private String image;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  public long getVaccineId() {
    return vaccineId;
  }

  public void setVaccineId(long vaccineId) {
    this.vaccineId = vaccineId;
  }

  public Long getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(Long doctorId) {
    this.doctorId = doctorId;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public Date getDateAdministered() {
    return dateAdministered;
  }

  public void setDateAdministered(Date dateAdministered) {
    this.dateAdministered = dateAdministered;
  }

  public boolean isAdministered() {
    return administered;
  }

  public void setAdministered(boolean administered) {
    this.administered = administered;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
