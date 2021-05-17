package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * A base for all request bodies
 */
abstract class BaseRequestBody {
    static final String FIELD_DEVICE = "device";
    @SerializedName(FIELD_DEVICE)
    String device;

    public abstract HashMap<String, String> getParamsHashMap();

    protected BaseRequestBody(String deviceId) {
        this.device = deviceId;
    }


}
