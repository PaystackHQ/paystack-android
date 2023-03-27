package co.paystack.android.api.model

import android.webkit.URLUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionApiResponse(
    @Json(name = "status")
    val status: String? = null,

    @Json(name = "message")
    val message: String? = null,

    @JvmField
    @Json(name = "reference")
    val reference: String? = null,

    @JvmField
    @Json(name = "trans")
    val trans: String? = null,

    @Json(name = "auth")
    val auth: String? = null,

    @Json(name = "otpmessage")
    val otpmessage: String? = null,

    @Json(name = "countryCode")
    val avsCountryCode: String? = null,
) {
    fun hasValidReferenceAndTrans(): Boolean {
        return reference != null && trans != null
    }

    fun hasValidUrl(): Boolean {
        return otpmessage != null && URLUtil.isValidUrl(otpmessage)
    }

    fun hasValidOtpMessage(): Boolean {
        return otpmessage != null
    }

    fun hasValidAuth(): Boolean {
        return auth != null
    }
}