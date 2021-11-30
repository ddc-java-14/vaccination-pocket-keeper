package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.vaccpocketkeeper.R;
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
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Database(
    entities = {User.class, Vaccine.class, Dose.class, Doctor.class},
    views = {VaccineSummary.class, DoseSummary.class},
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
        .addCallback(new VaccpocketkeeperDatabase.Callback(context))
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

  private static class Callback extends RoomDatabase.Callback {

    private final Context context;
    private VaccineRepository repository;

    private Callback(Context context) {
      this.context = context;
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      preloadDoctors();
    }

    private void preloadDoctors() {
      try (
          InputStream input = context.getResources().openRawResource(R.raw.doctors);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
      ) {
        List<Doctor> doctors = new LinkedList<>();
        for (CSVRecord record : parser) {
          Doctor doctor = new Doctor();
          doctor.setName(record.get(0));
          doctors.add(doctor);
        }
        VaccpocketkeeperDatabase
            .getInstance()
            .getDoctorDao()
            .insert(doctors)
            .subscribeOn(Schedulers.io())
            .subscribe();
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.getMessage(), e);
      }
    }

    private void preloadDoses() {
      try (
          InputStream input = context.getResources().openRawResource(R.raw.doses);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
      ) {
        List<Doctor> doctors = new LinkedList<>();
        for (CSVRecord record : parser) {
          Doctor doctor = new Doctor();
          doctor.setName(record.get(0));
          doctors.add(doctor);
        }
        VaccpocketkeeperDatabase
            .getInstance()
            .getDoctorDao()
            .insert(doctors)
            .subscribeOn(Schedulers.io())
            .subscribe();
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.getMessage(), e);
      }
    }

  }

}
