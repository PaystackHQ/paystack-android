package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/20/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class TokenException extends PaystackException {
    public TokenException(String message) {
        super(message);
    }
}
