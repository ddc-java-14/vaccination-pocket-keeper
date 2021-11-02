package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(tableName = "guess",
    indices = {
        @Index(value = {"service_key","name"})//column name, not field name
    },
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
  @SerializedName("id") //get id from server, but call serviceKey in gson.
  @ColumnInfo(name = "service_key", unique = true)
  private String serviceKey;

  @NonNull
  @ColumnInfo(index = true)
  private Date created;

  @ColumnInfo(name = "vaccine_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long vaccineId;

  @ColumnInfo(name = "doctor_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long doctorId;

  @NonNull
  @ColumnInfo(unique = true)
  private String name;

  @NonNull
  @ColumnInfo(name = "date_administered")
  private Date dateAdministered;

  @NonNull
  private String image;

}
