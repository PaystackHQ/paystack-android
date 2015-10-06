package co.paystack.android.exceptions;

import java.io.Serializable;

/**
 * Base class for exceptions
 * @author Segun Famisa {segunfamisa@gmail.com} on 9/13/15.
 */
public class PaystackException extends RuntimeException implements Serializable {

    public PaystackException(String message){
        super(message, null);
    }

    public PaystackException(String message, Throwable e) {
        super(message, e);
    }
}
