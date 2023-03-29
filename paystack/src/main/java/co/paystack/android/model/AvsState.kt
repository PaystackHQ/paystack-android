package co.paystack.android.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvsState(
    val name: String,
    val slug: String,
    val abbreviation: String
)