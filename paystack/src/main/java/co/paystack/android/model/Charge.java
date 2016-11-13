package co.paystack.android.model;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.paystack.android.exceptions.InvalidAmountException;
import co.paystack.android.exceptions.InvalidEmailException;

/**
 * Created by i on 24/08/2016.
 */
public class Charge extends PaystackModel {
    private final String TAG = Charge.class.getSimpleName();
    private Card card;
    private String email;
    private int amount;
    private JSONObject metadata;
    private JSONArray custom_fields;
    private boolean hasMeta = false;

    public Charge() {
        this.metadata = new JSONObject();
        this.custom_fields = new JSONArray();
        try {
            this.metadata.put("custom_fields", this.custom_fields);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
    }

    public enum Bearer {
        merchant, subaccount
    }

    private int transactionCharge;
    private String subaccount;
    private String reference;
    private Bearer bearer;

    public int getTransactionCharge() {
        return transactionCharge;
    }

    public Charge setTransactionCharge(int transactionCharge) {
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

    public Bearer getBearer() {
        return bearer;
    }

    public Charge setBearer(Bearer bearer) {
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

    public Charge putMetadata(String name, String value) throws JSONException{
        this.metadata.put(name, value);
        this.hasMeta = true;
        return this;
    }

    public Charge putCustomField(String displayName, String value) throws JSONException{
        JSONObject customObj = new JSONObject();
        customObj.put("value", value);
        customObj.put("display_name", displayName);
        customObj.put("variable_name", displayName.toLowerCase().replaceAll("[^a-z0-9 ]","_"));
        this.custom_fields.put(customObj);
        this.hasMeta = true;
        return this;
    }

    public String getMetadata(){
        if(!hasMeta){
            return null;
        }
        return this.metadata.toString();
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

    public Charge setAmount(int amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException(amount);
        this.amount = amount;
        return this;
    }
}
