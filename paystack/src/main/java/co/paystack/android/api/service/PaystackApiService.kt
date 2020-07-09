package co.paystack.android.api.service

import co.paystack.android.model.AvsState
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PaystackApiService {
    @GET("/address_verification/states")
    suspend fun getAddressVerificationStates(@Query("country") countryCode: String): List<AvsState>
}