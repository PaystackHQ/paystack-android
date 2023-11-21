package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/25/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class ExpiredAccessCodeException extends PaystackException {
    public ExpiredAccessCodeException(String message) {
        super(message);
    }
}
