package co.paystack.android.model;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import co.paystack.android.BuildConfig;
import co.paystack.android.Paystack;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.InvalidAmountException;
import co.paystack.android.exceptions.InvalidEmailException;

public class Charge extends PaystackModel {
    private final String TAG = Charge.class.getSimpleName();
    private Card card;
    private String email;
    private String access_code;
    private int amount;
    private JSONObject metadata;
    private JSONArray custom_fields;
    private boolean hasMeta = false;
    private HashMap<String, String> additionalParameters;
    private int transactionCharge;
    private String subaccount;
    private String reference;
    private Bearer bearer;
    private String currency;
    private String plan;

    private boolean localStarted = false;
    private boolean remoteStarted = false;

    private void beforeLocalSet(String fieldName){
        if(remoteStarted && BuildConfig.DEBUG){
            throw new ChargeException("You can not set " + fieldName + " after specifying an access code");
        }
        localStarted = true;
    }
    private void beforeRemoteSet(){
        if(localStarted && BuildConfig.DEBUG){
            throw new ChargeException("You can not set access code when providing transaction parameters in app");
        }
        remoteStarted = true;
    }
    public Charge() {
        this.metadata = new JSONObject();
        this.amount = -1;
        this.additionalParameters = new HashMap<>();
        this.custom_fields = new JSONArray();
        try {
            this.metadata.put("custom_fields", this.custom_fields);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
    }

    public void addParameter(String key, String value) {
        beforeLocalSet(key);
        this.additionalParameters.put(key, value);
    }

    public HashMap<String, String> getAdditionalParameters() {
        return additionalParameters;
    }

    public String getAccessCode() {
        return access_code;
    }

    public Charge setAccessCode(String access_code) {
        beforeRemoteSet();
        this.access_code = access_code;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Charge setCurrency(String currency) {
        beforeLocalSet("currency");
        this.currency = currency;
        return this;
    }

    public String getPlan() {
        return plan;
    }

    public Charge setPlan(String plan) {
        beforeLocalSet("plan");
        this.plan = plan;
        return this;
    }

    public int getTransactionCharge() {
        return transactionCharge;
    }

    public Charge setTransactionCharge(int transactionCharge) {
        beforeLocalSet("transaction charge");
        this.transactionCharge = transactionCharge;
        return this;
    }

    public String getSubaccount() {
        return subaccount;
    }

    public Charge setSubaccount(String subaccount) {
        beforeLocalSet("subaccount");
        this.subaccount = subaccount;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public Charge setReference(String reference) {
        beforeLocalSet("reference");
        this.reference = reference;
        return this;
    }

    public Bearer getBearer() {
        return bearer;
    }

    public Charge setBearer(Bearer bearer) {
        beforeLocalSet("bearer");
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
        beforeLocalSet("metadata");
        this.metadata.put(name, value);
        this.hasMeta = true;
        return this;
    }

    public Charge putMetadata(String name, JSONObject value) throws JSONException{
        beforeLocalSet("metadata");
        this.metadata.put(name, value);
        this.hasMeta = true;
        return this;
    }

    public Charge putCustomField(String displayName, String value) throws JSONException{
        beforeLocalSet("custom field");
        JSONObject customObj = new JSONObject();
        customObj.put("value", value);
        customObj.put("display_name", displayName);
        customObj.put("variable_name", displayName.toLowerCase(Locale.getDefault()).replaceAll("[^a-z0-9]","_"));
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
        beforeLocalSet("email");
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
        beforeLocalSet("amount");
        if (amount <= 0)
            throw new InvalidAmountException(amount);
        this.amount = amount;
        return this;
    }

    public enum Bearer {
        account, subaccount
    }
}
