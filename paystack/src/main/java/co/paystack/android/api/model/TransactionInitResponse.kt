package co.paystack.android.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionInitResponse(
    val status: String,

    @Json(name = "id")
    val transactionId: String
)
