package co.paystack.android.api.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@Keep
@JsonClass(generateAdapter = true)
data class ChargeResponse(

    val status: String?,

    @Json(name = "trans")
    val transactionId: String?,

    val reference: String?,

    val message: String?,

    @Json(name = "otpmessage")
    val otpMessage: String? = null,

    val auth: String? = null,

    @Json(name = "countryCode")
    val countryCode: String? = null,
) {

    companion object {
        fun fromJsonString(jsonString: String?): ChargeResponse {
            return try {
                Moshi.Builder().build()
                    .adapter(ChargeResponse::class.java)
                    .fromJson(jsonString) ?: error("Failed to parse charge response")
            } catch (e: Exception) {
                ChargeResponse(
                    status = "0",
                    transactionId = "",
                    reference = "",
                    message = e.message ?: "An error occurred while reading Auth data"
                )
            }
        }
    }
}