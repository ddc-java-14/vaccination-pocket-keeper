package edu.cnm.deepdive.vaccpocketkeeper.service;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.UserDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.UserWithVaccines;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

public class UserRepository {

  private final UserDao userDao;
  private final VaccineDao vaccineDao;


  public UserRepository() {
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    userDao = database.getUserDao();
    vaccineDao = database.getVaccineDao();
  }

  public LiveData<UserWithVaccines> get(long userId) {
    return userDao.select(userId);
  }

  public LiveData<List<User>> getAll() {
    return userDao.selectAll();
  }

  public Single<User> save(User user) {
    Single<User> task;
    if (user.getId() == 0) {
      user.setCreated(new Date());
      task = userDao
          .insert(user)
          .map((id) -> {
            user.setId(id);
            return user;
          }); //id of record added, then puts note back on the conveyor belt
    } else {
      task = userDao
          .update(user)
          .map((count) -> //count of records modified, then puts note back on the conveyor belt
              user
          );
    }
    return task.subscribeOn(
        Schedulers.io()); //give task back to viewModel to subscribe to (background thread)
  }

  public Completable delete(User user) {
    return (user.getId() == 0)
        //if id is 0, then the note has not been saved in the database, no point in deleting it
        ? Completable.complete()
        : userDao
            .delete(user)
            .ignoreElement() //not interested in how many records deleted; .ignore turns from single to completable.  or .flatmap (takes single and daisy chains completable to it)
            .subscribeOn(Schedulers.io());
  }

//  public Single<User> addNewVaccine() {
//    return Single
//        .fromCallable(() -> {
//          User user = new User();
//          return user;
//        }) //asynchronous call
//        .subscribeOn(Schedulers.io());
//  }


  @NonNull
  private Single<User> addUserWithVaccines(User user) {
    return userDao
        .insert(user)
        .map((id) -> {
          user.setId(id);
          return user;
        });
  }
}
