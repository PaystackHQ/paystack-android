package co.paystack.android.exceptions;

/**
 * Created by i on 24/08/2016.
 */
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
