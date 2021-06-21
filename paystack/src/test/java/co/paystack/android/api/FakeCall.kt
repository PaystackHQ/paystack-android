package co.paystack.android.api

import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakeCall<T>(private val response: Response<T>) : Call<T> {
    override fun clone(): Call<T> = this

    override fun execute(): Response<T> = response

    override fun enqueue(callback: Callback<T>) {
        // No-op
    }

    override fun isExecuted(): Boolean = false

    override fun cancel() {
        // No-op
    }

    override fun isCanceled(): Boolean = false

    override fun request(): Request? = null

    override fun timeout(): Timeout = Timeout.NONE

    companion object {
        const val TAG = "FakeCall"
        inline fun <reified T> success(body: T): FakeCall<T> {
            return FakeCall(Response.success(body))
        }

        inline fun <reified T> error(errorCode: Int, contentType: String, content: String): FakeCall<T> {
            return FakeCall(Response.error(errorCode, ResponseBody.create(MediaType.parse(contentType), content)))
        }
    }
}