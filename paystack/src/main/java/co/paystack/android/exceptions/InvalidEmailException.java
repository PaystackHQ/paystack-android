package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * Created by i on 24/08/2016.
 * @deprecated This SDK has been deprecated, Please refer to our new SDK: https://paystack.com/docs/developer-tools/android-sdk/
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class InvalidEmailException extends PaystackException {

    private String email;

    public InvalidEmailException(String email) {
        super(email + " is not a valid email");
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public InvalidEmailException setEmail(String email) {
        this.email = email;
        return this;
    }

}
