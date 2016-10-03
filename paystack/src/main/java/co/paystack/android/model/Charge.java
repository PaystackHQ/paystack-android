package co.paystack.android.model;

import android.util.Patterns;

import co.paystack.android.exceptions.InvalidEmailException;

/**
 * Created by i on 24/08/2016.
 */
public class Charge extends PaystackModel {
    private Card card;
    private String email;
    private int amount;

    private double transactionCharge;
    private String subaccount;
    private String reference;
    private String bearer;

    public double getTransactionCharge() {
        return transactionCharge;
    }

    public Charge setTransactionCharge(double transactionCharge) {
        this.transactionCharge = transactionCharge;
        return this;
    }

    public String getSubaccount() {
        return subaccount;
    }

    public Charge setSubaccount(String subaccount) {
        this.subaccount = subaccount;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public Charge setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getBearer() {
        return bearer;
    }

    public Charge setBearer(String bearer) {
        this.bearer = bearer;
        return this;
    }

    public Card getCard() {
        return card;
    }

    public Charge setCard(Card card) {
        this.card = card;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Charge setEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
        this.email = email;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Charge setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}
