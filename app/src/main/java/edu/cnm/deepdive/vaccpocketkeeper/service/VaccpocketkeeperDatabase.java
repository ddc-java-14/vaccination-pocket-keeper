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
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.VaccineWithDoses;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.DoseSummary;
import edu.cnm.deepdive.vaccpocketkeeper.model.view.VaccineSummary;
import edu.cnm.deepdive.vaccpocketkeeper.service.VaccpocketkeeperDatabase.Converters;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Defines a {@link Room} database object on which CRUD operations can be performed.
 */
@Database(
    entities = {User.class, Vaccine.class, Dose.class, Doctor.class},
    views = {VaccineSummary.class, DoseSummary.class},
    version = 1,
    exportSchema = true
) //when you uninstall an app, it wipes out the database too.
@TypeConverters({Converters.class})
public abstract class VaccpocketkeeperDatabase extends RoomDatabase {

  private static Application context;

  /**
   * Sets the application context to the local context variable.
   * @param context the Application context.
   */
  public static void setContext(Application context) {
    VaccpocketkeeperDatabase.context = context;
  }

  /**
   * Instantiates a single instande of the VaccpocketkeeperDatabase.
   * @return the {@link VaccpocketkeeperDatabase} instance.
   */
  public static VaccpocketkeeperDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * The declaration of a method that returns an instance of the {@link DoctorDao}.
   * @return an instance of the {@link DoctorDao}.
   */
  public abstract DoctorDao getDoctorDao();

  /**
   * The declaration of a method that returns an instance of the {@link DoseDao}.
   * @return an instance of the {@link DoseDao}.
   */
  public abstract DoseDao getDoseDao();

  /**
   * The declaration of a method that returns an instance of the {@link VaccineDao}.
   * @return an instance of the {@link VaccineDao}.
   */
  public abstract VaccineDao getVaccineDao();

  /**
   * The declaration of a method that returns an instance of the {@link UserDao}.
   * @return an instance of the {@link UserDao}.
   */
  public abstract UserDao getUserDao();

  private static class InstanceHolder {

    private static final VaccpocketkeeperDatabase INSTANCE = Room.databaseBuilder(
            context, VaccpocketkeeperDatabase.class, "vaccpocketkeeper-db")
        .addCallback(new VaccpocketkeeperDatabase.Callback(context))
        .build();
  }

  /**
   * A static class that converts {@link Date} objects to {@link Long} objects and vice versa.
   */
  public static class Converters {

    /**
     * converts a {@link Date} object to {@link Long} object.
     * @param value a {@link Date} object.
     * @return a {@link Long} object.
     */
    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    /**
     * converts a {@link Long} object to a {@link Date} object.
     * @param value a {@link Long} object.
     * @return a {@link Date} object.
     */
    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }
  }

  private static class Callback extends RoomDatabase.Callback {

    private final Context context;
    private VaccineRepository repository;
    private VaccineDao vaccineDao;
    private DoseDao doseDao;
    List<Dose> doses;

    private Callback(Context context) {
      this.context = context;
    }

    /**
     * Preloads the database with date obtained from CSV files.
     * @param db a {@link SupportSQLiteDatabase}.
     */
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
      vaccineDao = database.getVaccineDao();
      doseDao = database.getDoseDao();
      preloadDoctors();
      preloadVaccines();
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

    private void preloadVaccines() {
      try (
          InputStream input = context.getResources().openRawResource(R.raw.vaccines);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
      ) {
        //name, description, frequency, totalnumberofdoses, agerangelowerlimit, agerangeupperlimit
        List<Vaccine> vaccines = new LinkedList<>();
        doses = new LinkedList<>();

        for (CSVRecord record : parser) {
          VaccineWithDoses vaccine = new VaccineWithDoses();
          vaccine.setId(0);
          vaccine.setName(record.get(0).trim());
          vaccine.setDescription(record.get(1).trim());
          vaccine.setFrequency(Integer.parseInt(record.get(2).trim()));
          vaccine.setTotalNumberOfDoses(Integer.parseInt(record.get(3).trim()));
          vaccine.setAgeRangeLowerLimit(Integer.parseInt(record.get(4).trim()));
          vaccine.setAgeRangeUpperLimit(Integer.parseInt(record.get(5).trim()));

          Single<VaccineWithDoses> task;
          if (vaccine.getId() == 0) {
            vaccine.setCreated(new Date());
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < vaccine.getTotalNumberOfDoses();
                i++) { //TODO: getTotalNumberOfDoses is empty
              DoseWithDoctor dose = new DoseWithDoctor();
              cal.add(Calendar.YEAR, vaccine.getFrequency());
              dose.setDateAdministered(cal.getTime());
              dose.setCreated(new Date());
              dose.setName("DoseNumber" + (i + 1));
              vaccine.getDoses().add(dose);
            }
            vaccine.setDoses(vaccine.getDoses());//*****TODO check this out
            task = vaccineDao
                .insert(vaccine)
                .map((id) -> {
                  vaccine.setId(id);//id of record added, then puts note back on the conveyor belt
                  for (Dose dose : vaccine.getDoses()) {
                    dose.setVaccineId(id);
                    //dose.setId(0);
                    //preloadDoses();
                  }
                  return vaccine;
                })
                .flatMap((savedVaccine) -> doseDao.insert(savedVaccine.getDoses()))
                .map((ids) -> {
                  Iterator<Long> idIterator = ids.iterator();
                  Iterator<DoseWithDoctor> doseIterator = vaccine.getDoses().iterator();
                  while (idIterator.hasNext()) {
                    Dose dose = doseIterator.next();
                    Long id = idIterator.next();
                    dose.setId(id);
                  }
                  return vaccine;
                });
//            for (Dose dose : vaccine.getDoses()) {
//              dose.setVaccineId(vaccine.getId());
//              //dose.setId(0);
//              doses.add(dose);
//              VaccpocketkeeperDatabase
//                  .getInstance()
//                  .getDoseDao()
//                  .insert(dose)
//                  .subscribeOn(Schedulers.io())
//                  .subscribe();
//            }
          } else {
            task = vaccineDao
                .update(vaccine)
                .map(
                    (count) -> //count of records modified, then puts note back on the conveyor belt
                        vaccine
                );
          }
          task.subscribeOn(Schedulers.io());
//          vaccines.add(vaccine);
//        }
          VaccpocketkeeperDatabase
              .getInstance()
              .getVaccineDao()
              .insert(vaccine)
              .map((id) -> {
                for (Dose dose : vaccine.getDoses()) {
                  dose.setVaccineId(id);
                  VaccpocketkeeperDatabase
                  .getInstance()
                  .getDoseDao()
                  .insert(dose)
                  .subscribeOn(Schedulers.io())
                  .subscribe();
                }
                return vaccine;
              })
              .subscribeOn(Schedulers.io())
              .subscribe();
          }//****
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.getMessage(), e);
      }
      //preloadDoses();
    }

    private void preloadDoses() {
      VaccpocketkeeperDatabase
            .getInstance()
            .getDoseDao()
            .insert(doses)
            .subscribeOn(Schedulers.io())
            .subscribe();
    }
  }
}


