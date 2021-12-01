package edu.cnm.deepdive.vaccpocketkeeper.service;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.DoseDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.UserDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.dao.VaccineDao;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.User;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.UserWithVaccines;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates a persistent UserRepository object that allows the User ViewModel to interact with the UserDao to create, read, insert, and update data to the database.
 */
public class UserRepository {

  private final Application context;
  private final UserDao userDao;
  private final VaccineDao vaccineDao;


  /**
   * Constructor for class that instantiates a VaccpocketkeeperDatabase database object as well as local fields.
   * @param context the application context
   */
  public UserRepository(Application context) {
    this.context = context;
    VaccpocketkeeperDatabase database = VaccpocketkeeperDatabase.getInstance();
    userDao = database.getUserDao();
    vaccineDao = database.getVaccineDao();
  }

  /**
   * Interacts with the {@link UserDao} to get a {@link User} object from the database where
   * the user_id equals the user_id passed in from the caller.
   * @param userId a long primitive that uniquely identifies the {@link User} object.
   * @return a reactivex {@link LiveData} object of type {@link UserWithVaccines} object.
   */
  public LiveData<UserWithVaccines> get(long userId) {
    return userDao.select(userId);
  }

  /**
   * Interacts with the {@link UserDao} to get all {@link User} objects from the database.
   * @return a reactivex {@link LiveData} {@link List} of {@link User} objects.
   */
  public LiveData<List<User>> getAll() {
    return userDao.selectAll();
  }

  /**
   * Interacts with the {@link UserDao} to save a {@link User} object to the database.
   * If the user_id is zero, then it creates a new {@link User} object;
   * Otherwise, it saves the current {@link User} object.
   * @param user a {@link User} object that needs to be saved to the database
   * @return a reactivex {@link Single} of type {@link User}.
   */
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

  /**
   * Interacts with the {@link UserDao} to delete a {@link User} object from the database.
   * If the user_id is zero, then the {@link User} object has not been saved to the database;
   * Then there is no point in deleting it; Otherwise, deletes the {@link User} object from the database.
   * @param user a {@link User} object that needs to be deleted from the database
   * @return a reactivex {@link Completable}.
   */
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

  /**
   * Interacts with the {@link UserDao} to add a {@link UserWithVaccines} object to the database.
   * @param user a {@link User} object that needs to be saved to the database
   * @return a reactivex {@link Single} of type {@link User}.
   */
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
