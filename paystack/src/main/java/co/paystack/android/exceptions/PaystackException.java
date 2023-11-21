package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import java.io.Serializable;

import kotlin.Deprecated;

/**
 * Base class for exceptions
 *
 * @author {androidsupport@paystack.co} on 9/13/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class PaystackException extends RuntimeException implements Serializable {

    public PaystackException(String message) {
        super(message, null);
    }

    public PaystackException(String message, Throwable e) {
        super(message, e);
    }
}
