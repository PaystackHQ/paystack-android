package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Charge;
import co.paystack.android.utils.Crypto;
import co.paystack.android.utils.StringUtils;

/**
 * Charge Request Body
 */
public class ChargeRequestBody extends BaseRequestBody {

    private static final String FIELD_CLIENT_DATA = "clientdata";
    private static final String FIELD_LAST4 = "last4";
    private static final String FIELD_ACCESS_CODE = "access_code";
    private static final String FIELD_PUBLIC_KEY = "public_key";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_AMOUNT = "amount";
    private static final String FIELD_REFERENCE = "reference";
    private static final String FIELD_SUBACCOUNT = "subaccount";
    private static final String FIELD_TRANSACTION_CHARGE = "transaction_charge";
    private static final String FIELD_BEARER = "bearer";
    private static final String FIELD_HANDLE = "handle";
    private static final String FIELD_METADATA = "metadata";
    private static final String FIELD_CURRENCY = "currency";
    private static final String FIELD_PLAN = "plan";

    @SerializedName(FIELD_CLIENT_DATA)
    private final String clientData;

    @SerializedName(FIELD_LAST4)
    private final String last4;

    @SerializedName(FIELD_PUBLIC_KEY)
    private final String public_key;

    @SerializedName(FIELD_ACCESS_CODE)
    private final String access_code;

    @SerializedName(FIELD_EMAIL)
    private final String email;

    @SerializedName(FIELD_AMOUNT)
    private final String amount;

    @SerializedName(FIELD_REFERENCE)
    private final String reference;

    @SerializedName(FIELD_SUBACCOUNT)
    private final String subaccount;

    @SerializedName(FIELD_TRANSACTION_CHARGE)
    private final String transaction_charge;

    @SerializedName(FIELD_BEARER)
    private final String bearer;

    @SerializedName(FIELD_HANDLE)
    private String handle;

    @SerializedName(FIELD_METADATA)
    private String metadata;

    @SerializedName(FIELD_CURRENCY)
    private String currency;

    @SerializedName(FIELD_PLAN)
    private String plan;
    private HashMap<String, String> additionalParameters;

    public ChargeRequestBody(Charge charge) {
        this.setDeviceId();
        this.clientData = Crypto.encrypt(StringUtils.concatenateCardFields(charge.getCard()));
        this.last4 = charge.getCard().getLast4digits();
        this.public_key = PaystackSdk.getPublicKey();
        this.email = charge.getEmail();
        this.amount = Integer.toString(charge.getAmount());
        this.reference = charge.getReference();
        this.subaccount = charge.getSubaccount();
        this.transaction_charge = charge.getTransactionCharge() > 0 ? Integer.toString(charge.getTransactionCharge()) : null;
        this.bearer = charge.getBearer() != null ? charge.getBearer().name() : null;
        this.metadata = charge.getMetadata();
        this.plan = charge.getPlan();
        this.currency = charge.getCurrency();
        this.access_code = charge.getAccessCode();
        this.additionalParameters = charge.getAdditionalParameters();
    }

    public ChargeRequestBody addPin(String pin) {
        this.handle = Crypto.encrypt(pin);
        return this;
    }

    @Override
    public HashMap<String, String> getParamsHashMap() {
        // set values will override additional params provided
        HashMap<String, String> params = additionalParameters;
        params.put(FIELD_PUBLIC_KEY, public_key);
        params.put(FIELD_CLIENT_DATA, clientData);
        params.put(FIELD_LAST4, last4);
        if (access_code != null) {
            params.put(FIELD_ACCESS_CODE, access_code);
        }
        if (email != null) {
            params.put(FIELD_EMAIL, email);
        }
        if (amount != null) {
            params.put(FIELD_AMOUNT, amount);
        }
        if (handle != null) {
            params.put(FIELD_HANDLE, handle);
        }
        if (reference != null) {
            params.put(FIELD_REFERENCE, reference);
        }
        if (subaccount != null) {
            params.put(FIELD_SUBACCOUNT, subaccount);
        }
        if (transaction_charge != null) {
            params.put(FIELD_TRANSACTION_CHARGE, transaction_charge);
        }
        if (bearer != null) {
            params.put(FIELD_BEARER, bearer);
        }
        if (metadata != null) {
            params.put(FIELD_METADATA, metadata);
        }
        if (plan != null) {
            params.put(FIELD_PLAN, plan);
        }
        if (currency != null) {
            params.put(FIELD_CURRENCY, currency);
        }
        if (device != null) {
            params.put(FIELD_DEVICE, device);
        }
        return params;
    }
}
