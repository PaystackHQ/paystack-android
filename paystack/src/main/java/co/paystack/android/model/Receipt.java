package co.paystack.android.model;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import co.paystack.android.api.print.PrintBuilder;
import co.paystack.android.api.print.PrinterTemplate;

/**
 * Created by Oluwagbenga on 29,November,2018
 */
public class Receipt{

    //Purchase details
    private List<Purchase> purchases;
    private PrinterTemplate printerTemp;

    private String transactionDate;

    private ReceiptHeader receiptHeader;

    public Receipt(){}

    public Receipt(List<Purchase> purchases, ReceiptHeader receiptHeader, String transactionDate){
        this.purchases =purchases;
        this.receiptHeader = receiptHeader;
        this.transactionDate = transactionDate;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public Receipt addReceiptHeader(@Nullable ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
        return this;
    }


    public List<Purchase> getPurchases() {
        return purchases;
    }


    public Receipt addPurchases(List<Purchase> purchases) {
        this.purchases =purchases;
        return this;
    }


    public String getTransactionDate() {
        return transactionDate;
    }

    public Receipt addTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }



}
