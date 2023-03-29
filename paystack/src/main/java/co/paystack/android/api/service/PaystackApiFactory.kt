package co.paystack.android.mobilemoney.data.api

import android.os.Build
import co.paystack.android.BuildConfig
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.api.service.converter.WrappedResponseConverter
import co.paystack.android.api.utils.TLSSocketFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

/*
* Generates an API client for new paystack API (https://api.paystack.co)
* */
internal object PaystackApiFactory {
    private const val BASE_URL = "https://api.paystack.co/"

    @Throws(
        NoSuchAlgorithmException::class,
        KeyManagementException::class,
        KeyStoreException::class
    )
    fun createRetrofitService(): PaystackApiService {

        val tlsV1point2factory = TLSSocketFactory()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                // Add headers so we get Android version and Paystack Library version
                val builder = original.newBuilder()
                    .header(
                        "User-Agent",
                        "Android_" + Build.VERSION.SDK_INT + "_Paystack_" + BuildConfig.VERSION_NAME
                    )
                    .header("X-Paystack-Build", BuildConfig.VERSION_CODE.toString())
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                val request = builder.build()
                chain.proceed(request)
            }
            .sslSocketFactory(tlsV1point2factory, tlsV1point2factory.x509TrustManager)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(WrappedResponseConverter.Factory())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()

        return retrofit.create(PaystackApiService::class.java)
    }

}