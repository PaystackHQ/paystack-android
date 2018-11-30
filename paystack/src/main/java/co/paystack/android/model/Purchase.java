package co.paystack.android.model;

import android.support.annotation.RestrictTo;

/**
 * Created by Oluwagbenga on 29,November,2018
 *
 * This class holds the receipt's purchase data
 */
public class Purchase {

    private String item;
    private String quantity;
    private int subTotal;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
}
