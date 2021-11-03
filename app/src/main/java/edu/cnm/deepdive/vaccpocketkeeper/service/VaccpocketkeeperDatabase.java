package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoctorDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.UserDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Doctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.DoseSummary;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import edu.cnm.deepdive.vaccpocketkeeper.service.VaccpocketkeeperDatabase.Converters;
import java.util.Date;

@Database(
    entities = {User.class, Vaccine.class, Dose.class, Doctor.class},
    views = {VaccineSummary.class, DoseSummary.class},//TODO: come back to this
    version = 1,
    exportSchema = true
) //when you uninstall an app, it wipes out the database too.
@TypeConverters({Converters.class})
public abstract class VaccpocketkeeperDatabase extends RoomDatabase {

  private static Application context;

  public static void setContext(Application context) {
    VaccpocketkeeperDatabase.context = context;
  }

  public static VaccpocketkeeperDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract DoctorDao getDoctorDao();

  public abstract DoseDao getDoseDao();

  public abstract VaccineDao getVaccineDao();

  public abstract UserDao getUserDao();

  private static class InstanceHolder {

    private static final VaccpocketkeeperDatabase INSTANCE = Room.databaseBuilder(
            context, VaccpocketkeeperDatabase.class, "vaccpocketkeeper-db")
        .build();
  }

  public static class Converters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }
  }
}
