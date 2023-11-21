package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/22/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class PaystackSdkNotInitializedException extends PaystackException {
    public PaystackSdkNotInitializedException(String message) {
        super(message);
    }
}
