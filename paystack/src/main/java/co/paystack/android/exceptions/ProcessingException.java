package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * @author {androidsupport@paystack.co} on 9/25/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class ProcessingException extends ChargeException {
    public ProcessingException() {
        super("A transaction is currently processing, please wait till it concludes before attempting a new charge.");
    }
}
