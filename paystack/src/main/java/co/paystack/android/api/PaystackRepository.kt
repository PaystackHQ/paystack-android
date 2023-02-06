package co.paystack.android.api

import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.model.Charge
import co.paystack.android.ui.AddressHolder.Address

interface PaystackRepository {

    fun initializeTransaction(publicKey: String, charge: Charge, deviceId: String, callback: ApiCallback<TransactionInitResponse>)

    fun processCardCharge(chargeParams: ChargeParams, callback: ChargeApiCallback)

    fun validateTransaction(chargeParams: ChargeParams, token: String, callback: ChargeApiCallback)

    fun validateAddress(chargeParams: ChargeParams, address: Address, callback: ChargeApiCallback)

    fun requeryTransaction(chargeParams: ChargeParams, callback: ChargeApiCallback)

    fun getTransactionWithAccessCode(
        accessCode: String,
        callback: ApiCallback<TransactionInitResponse>
    )
}