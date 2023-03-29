package co.paystack.android.api.request

import co.paystack.android.api.utils.pruneNullValues
import co.paystack.android.model.Charge.Bearer
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionInitRequestBody(
    val publicKey: String,
    val email: String,
    val amount: Int,
    val currency: String?,
    val metadata: String?,
    val device: String,
    val reference: String?,
    val subAccount: String?,
    val transactionCharge: Int?,
    val plan: String?,
    val bearer: Bearer?,
    val additionalParameters: Map<String, String>,
) {
    fun toRequestMap() = additionalParameters + mapOf(
        FIELD_KEY to publicKey,
        FIELD_EMAIL to email,
        FIELD_AMOUNT to amount,
        FIELD_CURRENCY to currency,
        FIELD_METADATA to metadata,
        FIELD_DEVICE to device,
        FIELD_REFERENCE to reference,
        FIELD_SUBACCOUNT to subAccount,
        FIELD_TRANSACTION_CHARGE to transactionCharge,
        FIELD_BEARER to bearer?.name,
        FIELD_PLAN to plan,
    ).pruneNullValues()

    companion object {
        const val FIELD_KEY = "key"
        const val FIELD_EMAIL = "email"
        const val FIELD_AMOUNT = "amount"
        const val FIELD_CURRENCY = "currency"
        const val FIELD_METADATA = "metadata"
        const val FIELD_DEVICE = "device"

        const val FIELD_REFERENCE = "reference";
        const val FIELD_SUBACCOUNT = "subaccount";
        const val FIELD_TRANSACTION_CHARGE = "transaction_charge";
        const val FIELD_BEARER = "bearer";
        const val FIELD_PLAN = "plan";
    }
}
