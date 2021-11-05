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

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public List<Dose> getDoses() {
    return doses;
  }
}
