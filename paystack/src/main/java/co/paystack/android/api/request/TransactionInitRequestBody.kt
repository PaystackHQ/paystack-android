package co.paystack.android.api.request

import co.paystack.android.api.utils.pruneNullValues

data class TransactionInitRequestBody(
    val publicKey: String,
    val email: String,
    val amount: Int,
    val currency: String?,
    val metadata: String?,
    val device: String,
) {
    fun toRequestMap() = mapOf(
        FIELD_KEY to publicKey,
        FIELD_EMAIL to email,
        FIELD_AMOUNT to amount,
        FIELD_CURRENCY to currency,
        FIELD_METADATA to metadata,
        FIELD_DEVICE to device,
    ).pruneNullValues()

    companion object {
        const val FIELD_KEY = "key"
        const val FIELD_EMAIL = "email"
        const val FIELD_AMOUNT = "amount"
        const val FIELD_CURRENCY = "currency"
        const val FIELD_METADATA = "metadata"
        const val FIELD_DEVICE = "device"
    }
}
