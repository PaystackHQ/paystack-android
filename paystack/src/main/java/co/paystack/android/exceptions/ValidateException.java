package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * Created by i on 24/08/2016.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class ValidateException extends PaystackException {
    public ValidateException(String message) {
        super(message);
    }
}
