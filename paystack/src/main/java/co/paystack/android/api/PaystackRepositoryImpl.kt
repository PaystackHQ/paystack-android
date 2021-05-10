package co.paystack.android.api

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.api.request.TransactionInitRequestBody
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.model.Charge
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

internal class PaystackRepositoryImpl(private val apiService: PaystackApiService) : PaystackRepository {
    override fun initializeTransaction(publicKey: String, charge: Charge, callback: ApiCallback<TransactionInitResponse>) {
        val requestBody = TransactionInitRequestBody(
            publicKey = publicKey,
            email = charge.email,
            amount = charge.amount,
            currency = charge.currency,
            metadata = charge.metadata,
            device = UUID.randomUUID().toString().substring(0..8),
        ).toRequestMap()
        makeApiRequest(callback) {
            apiService.initializeTransaction(requestBody)
        }
    }

    override fun processCardCharge(chargeParams: ChargeParams, callback: ApiCallback<ChargeResponse>) {
        makeApiRequest(callback) {
            apiService.chargeCard(chargeParams.toRequestMap())
        }
    }

    private fun <T> makeApiRequest(apiCallback: ApiCallback<T>, apiCall: () -> Call<T>) {
        val retrofitCallback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody == null) {
                        apiCallback.onError(RuntimeException("No response body available"))
                        return
                    }

                    apiCallback.onSuccess(responseBody)
                } else {
                    apiCallback.onError(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                apiCallback.onError(t)
            }
        }

        apiCall().enqueue(retrofitCallback)
    }
}