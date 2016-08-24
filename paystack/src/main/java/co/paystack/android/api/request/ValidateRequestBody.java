package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by i on 24/08/2016.
 */
public class ValidateRequestBody extends BaseRequestBody implements Serializable {

    public static final String FIELD_TRANS = "trans";
    public static final String FIELD_TOKEN = "token";

    @SerializedName(FIELD_TRANS)
    public String trans;

    @SerializedName(FIELD_TOKEN)
    public String token;

    public ValidateRequestBody() {
    }

    public ValidateRequestBody(String trans, String token) {
        this.trans = trans;
        this.token = token;
    }

    @Override
    public HashMap<String, String> getParamsHashMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(FIELD_TRANS, trans);
        params.put(FIELD_TOKEN, token);
        return params;
    }
}