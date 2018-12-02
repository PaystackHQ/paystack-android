package co.paystack.android.model;

import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.List;

import co.paystack.android.utils.Utils;

/**
 * Created by Oluwagbenga on 29,November,2018
 *
 * This class holds the receipt's purchase data
 */
public class Purchase extends Charge{

    private String item;
    private String quantity;
    private String name;
    private String cardNumber;
    private String transactionStatus;
    private String transactionMessage;
    private List<Purchase> purchases;

    public static final String SUCCESS ="success";
    public static final String FAILED = "failed";


    public Purchase(){
        purchases = new ArrayList<>();
    }

    public String getTransactionMessage(){
        return transactionMessage;
    }

    public Purchase setTransactionMessage(String transactionMessage){
        this.transactionMessage = transactionMessage;
        return this;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public Purchase setTransactionStatus(String status){
        this.transactionStatus = status;
        return this;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public String getItem() {
        return item;
    }

    public Purchase setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
        return this;
    }

    public String getCardNumber(){return Utils.encrypt(cardNumber);}

    public Purchase setItem(String item) {
        this.item = item;
        return this;
    }

    public Purchase setCardName(String cardName){
        this.name = cardName;
        return this;
    }

    public String getName() {
        return name;
    }

    public Purchase setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public List<Purchase> build(){
        purchases.add(this);
        return purchases;
    }

}
