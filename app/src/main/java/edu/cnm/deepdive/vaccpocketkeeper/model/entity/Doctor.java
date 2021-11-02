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
    }
)
public class Doctor {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "dose_id") //type affinity, if the type doesn't match one of the types in SQLite, can use type affitinity; pimarykey is automatically indeed and automatically unique
  private long id;

  @NonNull
  @SerializedName("id") //get id from server, but call serviceKey in gson.
  @ColumnInfo(name = "service_key")
  private String serviceKey;

  @NonNull
  @ColumnInfo(index = true)
  private Date created;

  @NonNull
  @ColumnInfo(unique = true)
  private String name;
}
