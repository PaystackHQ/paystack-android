package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Charge Request Body
 */
public class ChargeRequestBody extends BaseRequestBody {

    public static final String FIELD_CLIENT_DATA = "clientdata";
    public static final String FIELD_LAST4 = "last4";
    public static final String FIELD_PUBLIC_KEY = "public_key";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_HANDLE = "handle";

    @SerializedName(FIELD_CLIENT_DATA)
    public String clientData;

    @SerializedName(FIELD_LAST4)
    public String last4;

    @SerializedName(FIELD_PUBLIC_KEY)
    public String public_key;

    @SerializedName(FIELD_EMAIL)
    public String email;

    @SerializedName(FIELD_AMOUNT)
    public String amount;

    @SerializedName(FIELD_HANDLE)
    public String handle;


    public ChargeRequestBody() {
    }

    public ChargeRequestBody(String clientData, String last4, String public_key, String email, String amount, String handle) {
        this.clientData = clientData;
        this.last4 = last4;
        this.public_key = public_key;
        this.email = email;
        this.amount = amount;
        this.handle = handle;
    }

    public ChargeRequestBody(String clientData, String last4, String public_key, String email, String amount) {
        this.clientData = clientData;
        this.last4 = last4;
        this.public_key = public_key;
        this.email = email;
        this.amount = amount;
    }

    public String getClientData() {
        return clientData;
    }

    public ChargeRequestBody setClientData(String clientData) {
        this.clientData = clientData;
        return this;
    }

    public String getLast4() {
        return last4;
    }

    public ChargeRequestBody setLast4(String last4) {
        this.last4 = last4;
        return this;
    }

    public String getPublic_key() {
        return public_key;
    }

    public ChargeRequestBody setPublic_key(String public_key) {
        this.public_key = public_key;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ChargeRequestBody setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public ChargeRequestBody setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getHandle() {
        return handle;
    }

    public ChargeRequestBody setHandle(String handle) {
        this.handle = handle;
        return this;
    }
}
