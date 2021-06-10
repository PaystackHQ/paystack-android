package co.paystack.android.api.di

import android.os.Build
import co.paystack.android.BuildConfig
import co.paystack.android.api.PaystackRepository
import co.paystack.android.api.PaystackRepositoryImpl
import co.paystack.android.api.service.ApiService
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.api.service.converter.WrappedResponseConverter
import co.paystack.android.api.utils.TLSSocketFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal fun apiComponent(): ApiComponent = ApiModule

internal interface ApiComponent {
    val gson: Gson
    val tlsV1point2factory: TLSSocketFactory
    val okHttpClient: OkHttpClient
    val legacyApiService: ApiService
    val paystackApiService: PaystackApiService
    val paystackRepository: PaystackRepository
}

internal object ApiModule : ApiComponent {
    const val LEGACY_API_URL = "https://standard.paystack.co/"
    const val PAYSTACK_API_URL = "https://studio-api.paystack.co/"

    override val gson = GsonBuilder()
        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
        .create()

    override val tlsV1point2factory = TLSSocketFactory()

    override val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            // Add headers so we get Android version and Paystack Library version
            val builder = original.newBuilder()
                .header("User-Agent", "Android_" + Build.VERSION.SDK_INT + "_Paystack_" + BuildConfig.VERSION_NAME)
                .header("X-Paystack-Build", BuildConfig.VERSION_CODE.toString())
                .header("Accept", "application/json")
                .method(original.method(), original.body())

            chain.proceed(builder.build())
        }
        .sslSocketFactory(tlsV1point2factory, tlsV1point2factory.getX509TrustManager())
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()


    override val legacyApiService: ApiService = Retrofit.Builder()
        .baseUrl(LEGACY_API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    override val paystackApiService: PaystackApiService = Retrofit.Builder()
        .baseUrl(PAYSTACK_API_URL)
        .client(okHttpClient)
        .addConverterFactory(WrappedResponseConverter.Factory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(PaystackApiService::class.java)

    override val paystackRepository: PaystackRepository = PaystackRepositoryImpl(paystackApiService)
}

