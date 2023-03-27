package co.paystack.android.api.request

import co.paystack.android.api.utils.pruneNullValues
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateTransactionParams(
    val transactionId: String,
    val token: String? = null,
    val deviceId: String? = null
) {
    fun toRequestMap() = mapOf(
        FIELD_TOKEN to token,
        FIELD_DEVICE to deviceId,
        FIELD_TRANS to transactionId,
    ).pruneNullValues()

    companion object {
        const val FIELD_TOKEN = "token"
        const val FIELD_DEVICE = "device"
        const val FIELD_TRANS = "trans"
    }
}
