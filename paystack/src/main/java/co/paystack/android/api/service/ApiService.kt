package co.paystack.android.api.service

import co.paystack.android.api.converter.Wrapped
import co.paystack.android.api.model.TransactionApiResponse
import co.paystack.android.api.model.TransactionInitResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * ApiService
 */
interface ApiService {
    @FormUrlEncoded
    @POST("/charge/mobile_charge")
    fun charge(@FieldMap fields: HashMap<String, String>): Call<TransactionApiResponse>

    @FormUrlEncoded
    @POST("/charge/validate")
    fun validateCharge(@FieldMap fields: HashMap<String, String>): Call<TransactionApiResponse>

    @GET("/requery/{trans}")
    fun requeryTransaction(@Path("trans") trans: String?): Call<TransactionApiResponse>

    @Wrapped
    @GET("/checkout/request_inline")
    suspend fun initiateTransaction(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): TransactionInitResponse
}