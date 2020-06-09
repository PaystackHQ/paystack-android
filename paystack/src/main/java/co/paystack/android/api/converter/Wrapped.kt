package co.paystack.android.api.converter

/**
 * This annotation indicates that the data expected from the response to a request is enveloped in another JSON object
 * Annotating a function for a Retrofit request with it triggers the [WrappedResponseConverter] to get and return the
 * data JSON object instead of trying to parse the whole response to the return type of the function
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Wrapped