package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(
    tableName = "vaccine",
    indices = {
        @Index(value = {"service_key"}, unique = true),
        @Index(value = {"name"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "user_id",
            childColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Vaccine {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "vaccine_id") //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long id;

  @NonNull
  @SerializedName("id") //get id from server, but call serviceKey in gson.
  @ColumnInfo(name = "service_key")
  private String serviceKey;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @ColumnInfo
  private String name = "";

  @NonNull
  private String description = "";

  @ColumnInfo(name = "user_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affinity; pimarykey is automatically indexed and automatically unique
  private long userId;

  @ColumnInfo(name = "frequency_first")
  private double frequencyFirst;

  @ColumnInfo(name = "out_of_frequency")
  private double outOfFrequency;

  @ColumnInfo(name = "age_range_lower_limit")
  private double ageRangeLowerLimit;

  @ColumnInfo(name = "age_range_upper_limit")
  private double ageRangeUpperLimit;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public String getServiceKey() {
    return serviceKey;
  }

  public void setServiceKey(@NonNull String serviceKey) {
    this.serviceKey = serviceKey;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getDescription() {
    return description;
  }

  public void setDescription(@NonNull String description) {
    this.description = description;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public double getFrequencyFirst() {
    return frequencyFirst;
  }

  public void setFrequencyFirst(double frequencyFirst) {
    this.frequencyFirst = frequencyFirst;
  }

  public double getOutOfFrequency() {
    return outOfFrequency;
  }

  public void setOutOfFrequency(double outOfFrequency) {
    this.outOfFrequency = outOfFrequency;
  }

  public double getAgeRangeLowerLimit() {
    return ageRangeLowerLimit;
  }

  public void setAgeRangeLowerLimit(double ageRangeLowerLimit) {
    this.ageRangeLowerLimit = ageRangeLowerLimit;
  }

  public double getAgeRangeUpperLimit() {
    return ageRangeUpperLimit;
  }

  public void setAgeRangeUpperLimit(double ageRangeUpperLimit) {
    this.ageRangeUpperLimit = ageRangeUpperLimit;
  }
}
