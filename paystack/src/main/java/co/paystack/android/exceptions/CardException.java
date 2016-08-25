package co.paystack.android.exceptions;

/**
 * @author {androidsupport@paystack.co} on 9/13/15.
 */
public class CardException extends PaystackException {

    public CardException(String message) {
        super(message);
    }

    public CardException(String message, Throwable e) {
        super(message, e);
    }
}
