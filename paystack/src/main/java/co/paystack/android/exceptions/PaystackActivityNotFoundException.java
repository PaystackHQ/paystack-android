package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/22/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class PaystackActivityNotFoundException extends PaystackException {
    public PaystackActivityNotFoundException(String message) {
        super(message);
    }

    public PaystackActivityNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
