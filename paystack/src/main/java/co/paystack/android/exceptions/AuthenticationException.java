package co.paystack.android.exceptions;

/**
 * @author {androidsupport@paystack.co} on 9/16/15.
 */
public class AuthenticationException extends PaystackException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
