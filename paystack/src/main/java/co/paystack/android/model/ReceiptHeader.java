package co.paystack.android.model;

import android.support.annotation.Nullable;

/**
 * Created by Oluwagbenga on 29,November,2018
 *
 * This class is responsible for what user want to display as header on receipt
 */
public class ReceiptHeader {

    private String businessName;
    private String address;
    private String email;
    private String phone;

    public ReceiptHeader(@Nullable String businessName, @Nullable String address,
                         @Nullable String email, @Nullable String phone){
        this.address = address;
        this.businessName = businessName;
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(@Nullable String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

}
