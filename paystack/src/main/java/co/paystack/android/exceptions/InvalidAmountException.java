package co.paystack.android.exceptions;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import kotlin.Deprecated;

/**
 * Created by i on 24/08/2016.
 * @deprecated This SDK has been deprecated, Please refer to our new SDK: https://paystack.com/docs/developer-tools/android-sdk/
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class InvalidAmountException extends PaystackException {

    private int amount;

    public InvalidAmountException(int amount) {
        super(amount + " is not a valid amount. only positive non-zero values are allowed.");
        this.setAmount(amount);
    }

    public int getAmount() {
        return amount;
    }

    public InvalidAmountException setAmount(int amount) {
        this.amount = amount;
        return this;
    }

}
