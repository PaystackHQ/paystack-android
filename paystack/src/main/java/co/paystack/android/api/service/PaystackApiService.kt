package co.paystack.android.api.service

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.service.converter.NoWrap
import co.paystack.android.model.AvsState
import retrofit2.Call
import retrofit2.http.*

internal interface PaystackApiService {
    @GET("/address_verification/states")
    suspend fun getAddressVerificationStates(@Query("country") countryCode: String): List<AvsState>

    @GET("/checkout/request_inline")
    fun initializeTransaction(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): Call<TransactionInitResponse>

    @GET("/transaction/verify_access_code/{accessCode}")
    fun getTransaction(@Path("accessCode") accessCode: String): Call<TransactionInitResponse>

    @FormUrlEncoded
    @POST("/checkout/card/charge")
    @NoWrap
    fun chargeCard(@FieldMap params: Map<String, @JvmSuppressWildcards Any>): Call<ChargeResponse>

    @NoWrap
    @FormUrlEncoded
    @POST("/checkout/card/validate")
    fun validateOtp(@FieldMap params: Map<String, @JvmSuppressWildcards Any>): Call<ChargeResponse>

    @NoWrap
    @FormUrlEncoded
    @POST("/checkout/card/avs")
    fun validateAddress(@FieldMap fields: Map<String, String>): Call<ChargeResponse>

    @NoWrap
    @GET("/checkout/requery/{transactionId}")
    fun requeryTransaction(@Path("transactionId") transactionId: String): Call<ChargeResponse>

}