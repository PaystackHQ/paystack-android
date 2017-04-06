package co.paystack.android.exceptions;

/**
 * @author {androidsupport@paystack.co} on 9/25/15.
 */
public class ExpiredAccessCodeException extends PaystackException {
    public ExpiredAccessCodeException(String message) {
        super(message);
    }
}
