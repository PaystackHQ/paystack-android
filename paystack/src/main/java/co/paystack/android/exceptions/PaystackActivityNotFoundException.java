package co.paystack.android.exceptions;

/**
 * @author {androidsupport@paystack.co} on 9/22/15.
 */
public class PaystackActivityNotFoundException extends PaystackException {
    public PaystackActivityNotFoundException(String message) {
        super(message);
    }

    public PaystackActivityNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
