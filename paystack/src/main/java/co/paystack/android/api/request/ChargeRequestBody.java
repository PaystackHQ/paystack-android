package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Charge;
import co.paystack.android.utils.StringUtils;

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


    private ChargeRequestBody() {
    }

    public ChargeRequestBody(Charge charge, String handle) {
        this(charge);
        this.handle = handle;
    }

    public ChargeRequestBody(Charge charge) {
        this.clientData = StringUtils.concatenateCardFields(charge.getCard());
        this.last4 = charge.getCard().getLast4digits();
        this.public_key = PaystackSdk.getPublicKey();
        this.email = charge.getEmail();
        this.amount = Integer.toString(charge.getAmount());
    }


    @Override
    public HashMap<String, String> getParamsHashMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(FIELD_PUBLIC_KEY, public_key);
        params.put(FIELD_CLIENT_DATA, clientData);
        params.put(FIELD_LAST4, last4);
        params.put(FIELD_EMAIL, email);
        params.put(FIELD_AMOUNT, amount);
        if (handle != null) {
            params.put(FIELD_HANDLE, handle);
        }

        return params;
    }
}
