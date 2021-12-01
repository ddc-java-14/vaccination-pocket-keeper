package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates a persistent Dcotor object with: id, created, name, and a {@link List} of doses.
 */
@Entity(tableName = "doctor",
    indices = {
        @Index(value = {"name"})
    }
)
public class Doctor {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "doctor_id") //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @ColumnInfo
  private String name = "";

  @Ignore
  private final List<Dose> doses = new LinkedList<>();

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

  public List<Dose> getDoses() {
    return doses;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
