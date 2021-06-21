package co.paystack.android.api

interface ApiCallback<T> {
    fun onSuccess(data: T)

    fun onError(exception: Throwable)
}