package co.paystack.android.model;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.paystack.android.api.print.PrintBuilder;
import co.paystack.android.api.print.PrinterTemplate;

/**
 * Created by Oluwagbenga on 29,November,2018
 */
public class Receipt extends PrintBuilder {

    //Purchase details
    private List<Purchase> purchases;
    private int total;
    private PrinterTemplate printerTemp;

    private Date transactionDate;

    private String transactionStatus;

    private ReceiptHeader receiptHeader;

    public Receipt(){}

    public Receipt(List<Purchase> purchases, ReceiptHeader receiptHeader,
                   int tax, int total,
                   String footerText, String cardType, Date transactionDate,
                   String transactionStatus){
        this.purchases =purchases;
        this.receiptHeader = receiptHeader;
        this.total = total;
        this.transactionDate = transactionDate;
        this.transactionStatus = transactionStatus;
    }


    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public Receipt addReciptHeader(@Nullable ReceiptHeader receiptHeader) {
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


    public Date getTransactionDate() {
        return transactionDate;
    }

    public Receipt addTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }



    public Receipt addTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public String getTransactionStatus() {
        return transactionStatus.toUpperCase();
    }

    public int getTotal() {
        return total;
    }

    public Receipt setTotal(int total) {
        this.total = total;
        return this;
    }


}
