package co.paystack.android.api.model

import com.google.gson.annotations.SerializedName

data class TransactionInitResponse(
    val status: String,

    @SerializedName("id")
    val transactionId: String
)
