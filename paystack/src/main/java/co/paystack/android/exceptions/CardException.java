package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/13/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class CardException extends PaystackException {

    public CardException(String message) {
        super(message);
    }

    public CardException(String message, Throwable e) {
        super(message, e);
    }
}
