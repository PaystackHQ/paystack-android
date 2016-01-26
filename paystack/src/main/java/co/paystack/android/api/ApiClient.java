package co.paystack.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.paystack.android.api.service.ApiService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setEndpoint(API_URL)
        .setConverter(new GsonConverter(gson))
        .build();

    apiService = restAdapter.create(ApiService.class);
  }

  public ApiService getApiService() {
    return apiService;
  }
}
