package co.paystack.android.api.model


import com.google.gson.annotations.SerializedName

data class TransactionInitResponse(
    val id: Long,
    @SerializedName("access_code")
    val accessCode: String,
    val currency: String,
    val amount: Int,
    val email: String,
    val channels: List<String>,
    @SerializedName("merchant_name")
    val merchantName: String,
    @SerializedName("merchant_id")
    val merchantId: Int,
    @SerializedName("transaction_status")
    val transactionStatus: String,
    @SerializedName("payment_page")
    val paymentPage: Int,

    @SerializedName("skip_pin_check_for_card_types")
    val skipPinCheckForCardTypes: List<String>
)