package co.paystack.android.api;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import co.paystack.android.BuildConfig;
import co.paystack.android.api.service.ApiService;
import co.paystack.android.api.utils.TLSSocketFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API Client Class
 * <p>
 * Provides a service by which we can make API calls</p>
 */
public class ApiClient {

    private static final String BASE_URL = "https://standard.paystack.co/";
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
                                .header("User-Agent", "Android_" + Build.VERSION.SDK_INT + "_Paystack_" + BuildConfig.VERSION_NAME)
                                .header("X-Paystack-Build", String.valueOf(BuildConfig.VERSION_CODE))
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());
                        Request request = builder.build();

                        return chain.proceed(request);
                    }
                })
                .sslSocketFactory(tlsV1point2factory, tlsV1point2factory.getX509TrustManager())
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
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
