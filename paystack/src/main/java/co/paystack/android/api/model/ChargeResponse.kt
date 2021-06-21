package co.paystack.android.api.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChargeResponse(

    val status: String?,

    @SerializedName("trans")
    val transactionId: String,

    val reference: String,

    val message: String?,

    @SerializedName("otpmessage")
    val otpMessage: String? = null,

    val auth: String? = null,

    @SerializedName("countryCode")
    val countryCode: String? = null

)