package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

public class ValidateRequestBody extends BaseRequestBody implements Serializable {

    private static final String FIELD_TRANS = "trans";
    private static final String FIELD_TOKEN = "token";

    @SerializedName(FIELD_TRANS)
    private String trans;

    @SerializedName(FIELD_TOKEN)
    private String token;

    public ValidateRequestBody() {
        this.setDeviceId();
    }

    private String getTrans() {
        return trans;
    }

    public ValidateRequestBody setTrans(String trans) {
        this.trans = trans;
        return this;
    }

    private String getToken() {
        return token;
    }

    public ValidateRequestBody setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public HashMap<String, String> getParamsHashMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(FIELD_TRANS, getTrans());
        params.put(FIELD_TOKEN, getToken());
        if (device != null) {
            params.put(FIELD_DEVICE, device);
        }
        return params;
    }
}