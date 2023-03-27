package co.paystack.android.api

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.request.ChargeParams

interface ChargeApiCallback {
    fun onSuccess(params: ChargeParams, response: ChargeResponse)

    fun onError(exception: Throwable, reference: String?)
}