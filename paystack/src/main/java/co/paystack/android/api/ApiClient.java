package co.paystack.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.paystack.android.api.service.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import retrofit2.RestAdapter;
//import retrofit2.converter.GsonConverter;

/**
 * API Client Class
 *
 * Provides a service by which we can make API calls
 */
public class ApiClient {

  private static final String BASE_URL = "https://standard.paystack.co/";
  public static String API_URL = BASE_URL;

  private ApiService apiService;

  public ApiClient() {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
//    RestAdapter restAdapter = new RestAdapter.Builder()
//        .setLogLevel(RestAdapter.LogLevel.FULL)
//        .setEndpoint()
//        .setConverter(new GsonConverter(gson))
//        .build();

    apiService = retrofit.create(ApiService.class);
  }

  public ApiService getApiService() {
    return apiService;
  }
}
