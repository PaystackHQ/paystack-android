package co.paystack.android.api.request

import co.paystack.android.Transaction
import co.paystack.android.api.utils.pruneNullValues
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChargeParams(
    val clientData: String,
    val transactionId: String,
    val last4: String,
    val deviceId: String,
    val reference: String?,
    val handle: String? = null
) {
    fun toRequestMap() = mapOf(
        FIELD_CLIENT_DATA to clientData,
        FIELD_HANDLE to handle,
        FIELD_TRANS to transactionId,
        FIELD_LAST4 to last4,
        FIELD_DEVICE to deviceId,
        FIELD_SOURCE to "android"
    ).pruneNullValues()

    fun addPin(pin: String) = copy(handle = pin)

    fun getTransaction(): Transaction {
        val transaction = Transaction()
        transaction.setId(transactionId)
        transaction.reference = reference
        return transaction
    }

    companion object {
        const val FIELD_CLIENT_DATA = "clientdata"
        const val FIELD_HANDLE = "handle"
        const val FIELD_TRANS = "trans"
        const val FIELD_LAST4 = "last4"
        const val FIELD_SOURCE = "source"
        const val FIELD_DEVICE = "device"
    }
}