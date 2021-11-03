package edu.cnm.deepdive.vaccpocketkeeper.service;

import androidx.annotation.NonNull;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;

public class VaccineRepository {

  private final WebServiceProxy proxy;
  private final VaccineDao vaccineDao;
  private final DoseDao doseDao;

  public VaccineRepository() {
    proxy = WebServiceProxy.getInstance();
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    vaccineDao = database.getVaccineDao();
    doseDao = database.getDoseDao();
  }

  public Single<Vaccine> insertNewVaccine() {
    return Single
        .fromCallable(() -> {
          Vaccine vaccine = new Vaccine();
          return vaccine;
        }) //asynchronous call
        .flatMap(proxy::insertNewVaccine)
        .subscribeOn(Schedulers.io());
  }

  public Single<Vaccine> insertDosesForVaccine(Vaccine vaccine) {
    Calendar cal = Calendar.getInstance();
    //Date today = cal.getTime();
    //cal.add(Calendar.YEAR, 1); // to get previous year add 1
    //cal.add(Calendar.DAY_OF_MONTH, -1); // to get previous day add -1
    //Date expiryDate = cal.getTime();
    return Single
        .fromCallable(() -> {
          for (int i = 0; i < vaccine.getTotalNunberOfDoses(); i++) {
            Dose dose = new Dose();
            cal.add(Calendar.YEAR, vaccine.getFrequency());
            dose.setDateAdministered(cal.getTime());
            vaccine.getDoses().add(dose);
          }
          return vaccine;
        })
        //.flatMap((dose) -> proxy.insertNewDose(dose, vaccine.getServiceKey()))//TODO: come back to this
//        .map((dose) -> {
//          vaccine.setSolved(dose.isSolution());
//          return vaccine;
//        })
        .flatMap(this::insertVaccineWithDoses)
        .subscribeOn(Schedulers.io());
  }

//  public LiveData<List<GameSummary>> selectSummariesByGuessCount(int poolSize, int length) {
//    return vaccineDao.selectSummariesByGuessCount(poolSize, length);
//  }

//  public LiveData<List<GameSummary>> selectSummariesByTotalTime(int poolSize, int length) {
//    return vaccineDao.selectSummariesByTotalTime(poolSize, length);
//  }

  @NonNull
  private Single<Vaccine> insertVaccineWithDoses(Vaccine vaccine) {
    return vaccineDao
        .insert(vaccine)
        .map((id) -> {
          vaccine.setId(id);
          for (Dose dose : vaccine.getDoses()) {
            dose.setVaccineId(id);
          }
          return vaccine;
        })
        .flatMap((vaccine2) -> doseDao
            .insert(vaccine2.getDoses())
            //TODO invoke Dose.setId for all of the doses
            .map((ids) -> vaccine2));
    //: Single.just(vaccine);
  }
}
