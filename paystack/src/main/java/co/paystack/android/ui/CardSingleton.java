package co.paystack.android.ui;

import co.paystack.android.model.Card;

public class CardSingleton {
    private static CardSingleton instance = new CardSingleton();
    private Card card = null;

    private CardSingleton() {
    }

    public static CardSingleton getInstance() {
        return instance;
    }

    public Card getCard() {
        return card;
    }

    public CardSingleton setCard(Card card) {
        this.card = card;
        return this;
    }


}