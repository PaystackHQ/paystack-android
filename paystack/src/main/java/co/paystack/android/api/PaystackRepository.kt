package co.paystack.android.api

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.model.Charge

interface PaystackRepository {

    fun initializeTransaction(publicKey: String, charge: Charge, callback: ApiCallback<TransactionInitResponse>)

    fun processCardCharge(chargeParams: ChargeParams, callback: ApiCallback<ChargeResponse>)
}