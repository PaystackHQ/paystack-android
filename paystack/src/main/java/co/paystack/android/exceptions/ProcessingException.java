package co.paystack.android.exceptions;

/**
 * @author {androidsupport@paystack.co} on 9/25/15.
 */
public class ProcessingException extends ChargeException {
    public ProcessingException() {
        super("A transaction is currently processing, please wait till it concludes before attempting a new charge.");
    }
}
