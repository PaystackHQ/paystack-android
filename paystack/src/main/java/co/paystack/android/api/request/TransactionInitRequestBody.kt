package co.paystack.android.api.request

import co.paystack.android.api.utils.pruneNullValues

data class TransactionInitRequestBody(
    val publicKey: String,
    val email: String,
    val amount: Int,
    val currency: String?,
    val metadata: String,
    val device: String,
) {
    fun toRequestMap() = mapOf(
        "key" to publicKey,
        "email" to email,
        "amount" to amount,
        "currency" to currency,
        "metadata" to metadata,
        "device" to device,
    ).pruneNullValues()
}
