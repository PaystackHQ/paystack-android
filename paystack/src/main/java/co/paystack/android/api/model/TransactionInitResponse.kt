package co.paystack.android.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionInitResponse(
    @Json(name = "id")
    val transactionId: String,

    @Json(name = "reference")
    val reference: String?
)
