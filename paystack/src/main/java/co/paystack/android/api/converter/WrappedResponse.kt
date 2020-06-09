package co.paystack.android.api.converter

open class WrappedResponse<T>(
    val `data`: T,
    val message: String,
    val status: Boolean
)