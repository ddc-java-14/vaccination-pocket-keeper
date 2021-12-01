package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates a persistent Vaccine object with: id, created, name, description, userId, frequency, totalNumberOfDoses, ageRangeLowerLimit, and ageRangeUpperLimit.
 */
@Entity(
    tableName = "vaccine",
    indices = {
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
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @ColumnInfo
  private String name = "";

  @NonNull
  private String description = "";

  @ColumnInfo(name = "user_id", index = true) //type affinity, if the type doesn't match one of the types in SQLite, can use type affinity; pimarykey is automatically indexed and automatically unique
  private Long userId; // FIXME: make this a primitive

  private int frequency;

  @ColumnInfo(name = "total_number_of_doses")
  private int totalNumberOfDoses;

  @ColumnInfo(name = "age_range_lower_limit")
  private int ageRangeLowerLimit;

  @ColumnInfo(name = "age_range_upper_limit")
  private int ageRangeUpperLimit;

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
   * Returns the local description field.
   */
  @NonNull
  public String getDescription() {
    return description;
  }
  
  /**
   * Sets the local description field to the description parameter provided.
   */
  public void setDescription(@NonNull String description) {
    this.description = description;
  }

  /**
   * Returns the local userId field.
   */
  public Long getUserId() {
    return userId;
  }// FIXME: make this get a primitive

  /**
   * Sets the local userId field to the userId parameter.
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }// FIXME: make this return a primitive

  /**
   * Returns the local frequency field.
   */  
  public int getFrequency() {
    return frequency;
  }

  /**
   * Sets the local frequency field to the frequency parameter.
   */
  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  /**
   * Returns the local totalNumberOfDoses field.
   */
  public int getTotalNumberOfDoses() {
    return totalNumberOfDoses;
  }

  /**
   * Sets the local totalNumberOfDoses field to the totalNumberOfDoses parameter.
   */
  public void setTotalNumberOfDoses(int totalNumberOfDoses) {
    this.totalNumberOfDoses = totalNumberOfDoses;
  }

  /**
   * Returns the local ageRangeLowerLimit field.
   */
  public int getAgeRangeLowerLimit() {
    return ageRangeLowerLimit;
  }

  /**
   * Sets the local ageRangeLowerLimit field to the ageRangeLowerLimit parameter.
   */
  public void setAgeRangeLowerLimit(int ageRangeLowerLimit) {
    this.ageRangeLowerLimit = ageRangeLowerLimit;
  }

  /**
   * Returns the local ageRangeUpperLimit field.
   */
  public int getAgeRangeUpperLimit() {
    return ageRangeUpperLimit;
  }

  /**
   * Sets the local ageRangeUpperLimit field to the ageRangeUpperLimit parameter.
   */
  public void setAgeRangeUpperLimit(int ageRangeUpperLimit) {
    this.ageRangeUpperLimit = ageRangeUpperLimit;
  }

}
