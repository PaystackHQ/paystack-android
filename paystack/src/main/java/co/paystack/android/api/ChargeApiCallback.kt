package co.paystack.android.api

import co.paystack.android.DEPRECATION_MESSAGE
import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.request.ChargeParams

@Deprecated(message = DEPRECATION_MESSAGE)
interface ChargeApiCallback {
    fun onSuccess(params: ChargeParams, response: ChargeResponse)

    fun onError(exception: Throwable, reference: String?)
}
