package co.paystack.android.api

import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.api.request.TransactionInitRequestBody
import co.paystack.android.api.request.ValidateTransactionParams
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.model.Charge
import co.paystack.android.ui.AddressHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

internal class PaystackRepositoryImpl(private val apiService: PaystackApiService) : PaystackRepository {
    override fun initializeTransaction(publicKey: String, charge: Charge, deviceId: String, callback: ApiCallback<TransactionInitResponse>) {
        val requestBody = TransactionInitRequestBody(
            publicKey = publicKey,
            email = charge.email,
            amount = charge.amount,
            currency = charge.currency,
            metadata = charge.metadata,
            device = deviceId,
            reference = charge.reference,
            subAccount = charge.subaccount,
            transactionCharge = charge.transactionCharge,
            plan = charge.plan,
            bearer = charge.bearer,
            additionalParameters = charge.additionalParameters,
        ).toRequestMap()

        makeApiRequest(
            onSuccess = { data -> callback.onSuccess(data) },
            onError = { throwable -> callback.onError(throwable) },
            apiCall = { apiService.initializeTransaction(requestBody) }
        )
    }

    override fun processCardCharge(chargeParams: ChargeParams, callback: ChargeApiCallback) {
        makeApiRequest(
            onSuccess = { data -> callback.onSuccess(chargeParams, data) },
            onError = { throwable -> callback.onError(throwable, chargeParams.reference) },
            apiCall = { apiService.chargeCard(chargeParams.toRequestMap()) }
        )
    }

    override fun validateTransaction(chargeParams: ChargeParams, token: String, callback: ChargeApiCallback) {
        val requestBody = ValidateTransactionParams(
            transactionId = chargeParams.transactionId,
            token = token,
            deviceId = chargeParams.deviceId,
        ).toRequestMap()

        makeApiRequest(
            apiCall = { apiService.validateOtp(requestBody) },
            onSuccess = { data -> callback.onSuccess(chargeParams, data) },
            onError = { throwable -> callback.onError(throwable, chargeParams.reference) },
        )
    }

    override fun requeryTransaction(chargeParams: ChargeParams, callback: ChargeApiCallback) {
        makeApiRequest(
            apiCall = { apiService.requeryTransaction(chargeParams.transactionId) },
            onSuccess = { data -> callback.onSuccess(chargeParams, data) },
            onError = { throwable -> callback.onError(throwable, chargeParams.reference) },
        )
    }

    override fun validateAddress(chargeParams: ChargeParams, address: AddressHolder.Address, callback: ChargeApiCallback) {
        val requestBody = address.toHashMap()
        requestBody["trans"] = chargeParams.transactionId

        makeApiRequest(
            apiCall = { apiService.validateAddress(requestBody) },
            onSuccess = { data -> callback.onSuccess(chargeParams, data) },
            onError = { throwable -> callback.onError(throwable, chargeParams.reference) },
        )
    }

    override fun getTransactionWithAccessCode(accessCode: String, callback: ApiCallback<TransactionInitResponse>) {
        makeApiRequest(
            onSuccess = { data -> callback.onSuccess(data) },
            onError = { throwable -> callback.onError(throwable) },
            apiCall = { apiService.getTransaction(accessCode) }
        )
    }

    private fun <T> makeApiRequest(apiCall: () -> Call<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        val retrofitCallback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody == null) {
                        onError(RuntimeException("No response body available"))
                        return
                    }

                    onSuccess(responseBody)
                } else {
                    onError(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t)
            }
        }

        apiCall().enqueue(retrofitCallback)
    }
}