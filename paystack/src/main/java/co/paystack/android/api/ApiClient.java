package co.paystack.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import co.paystack.android.BuildConfig;
import co.paystack.android.Config;
import co.paystack.android.api.service.ApiService;
import co.paystack.android.api.utils.TLSSocketFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private static final String BASE_URL = "https://crayon.paystack.co/";
  public static String API_URL = BASE_URL;

  private ApiService apiService;

  public ApiClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
        .create();

    TLSSocketFactory tlsV1point2factory = new TLSSocketFactory();
    OkHttpClient okHttpClient = new OkHttpClient
        .Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    // Add headers so we get Android version and Paystack Library version
                    Request.Builder builder = original.newBuilder()
                            .header("User-Agent", "Android_" + Config.VERSION_CODE + "_Paystack_" + BuildConfig.VERSION_NAME)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());
                    Request request = builder.build();

                    return chain.proceed(request);
                }
            })
        .sslSocketFactory(tlsV1point2factory, tlsV1point2factory.getX509TrustManager())
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    apiService = retrofit.create(ApiService.class);
  }

  public ApiService getApiService() {
    return apiService;
  }
}
