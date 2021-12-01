package edu.cnm.deepdive.vaccpocketkeeper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates a persistent question object with: id, created, name, email, and birthday.
 */
@Entity(tableName = "user",
    indices = {
        @Index(value = {"name"}),
        @Index(value = {"email"}, unique = true)
    }
)
public class User {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "user_id") //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @ColumnInfo
  private String name = "";

  @NonNull
  @ColumnInfo
  private String email = "";

  @NonNull
  @ColumnInfo
  private Date birthday = new Date();

  /**
   * This method returns the local id field.
   */
  public long getId() {
    return id;
  }

  /**
   * This method sets the local id field to the id parameter
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * This method returns the local created {@link Date} field
   */
  @NonNull
  public Date getCreated() {
    return created;
  }

  /**
   * This method set the local created {@link Date} field to the created {@link Date} argument.
   */
  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  /**
   * This method returns the local name field.
   */
  @NonNull
  public String getName() {
    return name;
  }

  /**
   * This method sets the local field name to the name parameter provided.
   */
  public void setName(@NonNull String name) {
    this.name = name;
  }

  /**
   * This method returns the local email field.
   */
  @NonNull
  public String getEmail() {
    return email;
  }

  /**
   * This method sets the local email field to the email parameter provided.
   */
  public void setEmail(@NonNull String email) {
    this.email = email;
  }

  /**
   * This method returns the local birthday field.
   */
  @NonNull
  public Date getBirthday() {
    return birthday;
  }

  /**
   * This method sets the local birthday field to the birthday {@link Date} parameter provided.
   */
  public void setBirthday(@NonNull Date birthday) {
    this.birthday = birthday;
  }

}
