package co.paystack.android.api.service.converter

import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class WrappedResponseConverter<T>(
    private val delegate: Converter<ResponseBody, WrappedResponse<T>>
) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        val response = delegate.convert(value)
        return response?.data
    }


    class Factory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {

            // Should not use this converter if function is annotated with [NoWrap]
            if (annotations.any { it is NoWrap }) {
                return null
            }

            val wrappedType: Type = object : ParameterizedType {
                override fun getRawType(): Type {
                    return WrappedResponse::class.java
                }

                override fun getOwnerType(): Type? {
                    return null
                }

                override fun getActualTypeArguments(): Array<Type> {
                    return arrayOf(type)
                }
            }

            val delegate = retrofit.nextResponseBodyConverter<WrappedResponse<Any>>(this, wrappedType, annotations)
            return WrappedResponseConverter(delegate)

        }
    }
}

@JsonClass(generateAdapter = true)
open class WrappedResponse<T>(
    val `data`: T,
    val message: String,
    val status: Boolean
)