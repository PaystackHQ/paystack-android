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
    private int tax;
    private int total;
    private String companyLogo;
    private String footerText;
    private String cardType;
    private PrinterTemplate printerTemp;

    private Date transactionDate;

    private String transactionStatus;

    private ReceiptHeader receiptHeader;

    private String cardNo;

    private String companyName;

    public Receipt(){}

    public Receipt(List<Purchase> purchases, ReceiptHeader receiptHeader,
                   int tax, int total,
                   String footerText, String cardType, Date transactionDate,
                   String transactionStatus){
        this.purchases =purchases;
        this.receiptHeader = receiptHeader;
        this.tax =tax;
        this.total = total;
        this.footerText =footerText;
        this.cardType = cardType;
        this.transactionDate = transactionDate;
        this.transactionStatus = transactionStatus;
    }

    public Receipt addCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Receipt addCompanyLogo(String companyLogo){
        this.companyLogo = companyLogo;
        return this;
    }

    public String getCompanyLogo() {
        return TextUtils.isEmpty(companyLogo) ? "":companyLogo;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public Receipt addReciptHeader(@Nullable ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
        return this;
    }

    public Receipt addFooterText(@Nullable String footerText) {
        this.footerText = footerText;
        return this;
    }

    public String getFooterText() {
        return footerText;
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

    public String getCardType() {
        return cardType;
    }

    public Receipt addCardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public Receipt addTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public String getTransactionStatus() {
        return transactionStatus.toUpperCase();
    }

    public int getTax() {
        return tax;
    }

    public Receipt addTax(int tax) {
        this.tax = tax;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Receipt setTotal(int total) {
        this.total = total;
        return this;
    }

    public Receipt addCardNo(String cardNo){
        this.cardNo =cardNo;
        return this;
    }

    public String getCardNo() {
        return TextUtils.isEmpty(cardNo)? "N/A":encrypt(cardNo);
    }


    private static String encrypt(String text){
        if (text.length() > 3){
            String start = text.substring(0, 3);
            int l = text.length() -start.length();
            StringBuilder starBuilder = new StringBuilder();
            char mChar;
            if(l >-1){
                for(int i=start.length(); i < text.length(); i++){
                    if(i < text.length()-2){
                        mChar ='*';
                    }else{
                        mChar =text.charAt(i);
                    }
                    starBuilder.append(String.valueOf(mChar));
                }
            }

            return start.concat(starBuilder.toString());
        }
        return text;
    }


}
