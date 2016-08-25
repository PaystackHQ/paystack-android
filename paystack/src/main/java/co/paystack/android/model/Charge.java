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
