package co.paystack.android.api.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChargeResponse(

    val status: String,

    @SerializedName("trans")
    val transactionId: String,

    val reference: String,

    val message: String,

    val response: String,

//
//    val auth: String,
//
//
//
//
//
//
//    val bank: String,
//
//
//
//    @SerializedName("otpmessage")
//    val otpMessage: String,
//
//    @SerializedName("redirecturl")
//    val redirectUrl: String,
//
//
//
//    @SerializedName("countrycode")
//    val countryCode: String,

)