package co.paystack.android.exceptions;

/**
 * @author Segun Famisa {segunfamisa@gmail.com} on 9/13/15.
 */
public class CardException extends PaystackException {

    public CardException(String message) {
        super(message);
    }

    public CardException(String message, Throwable e) {
        super(message, e);
    }
}
