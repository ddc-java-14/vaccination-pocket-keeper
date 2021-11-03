package edu.cnm.deepdive.vaccpocketkeeper.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.vaccpocketkeeper.BuildConfig;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Vaccine;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceProxy {

  // What is the path that we are going to send this? Note: this is relative to base url from retrofit below
  @POST("vaccines")
  Single<Vaccine> insertNewVaccine(@Body Vaccine vaccine); //send vaccine request in body of request.

  @POST("vaccines/{vaccineId}/doses")
  Single<Dose> insertNewDose(@Body Dose dose, @Path("vaccineId") String vaccineId); //name of placeholder where gameId goes in the path

  static WebServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final WebServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") //to GSON: when you receive a data-time, format it, when you send it, send it this way
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //instead of using a call object when invoke send and receive a task of one of those Rx java types (i.e. single, repeatable, flowable, maybe)
          .client(client)
          .build();
      INSTANCE = retrofit.create(WebServiceProxy.class);
    }
  }
}
