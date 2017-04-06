package co.paystack.android.api.request;

import android.provider.Settings;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import co.paystack.android.PaystackSdk;

/**
 * A base for all request bodies
 */
abstract class BaseRequestBody {
    static final String FIELD_DEVICE = "device";
    @SerializedName(FIELD_DEVICE)
    String device;

    public abstract HashMap<String, String> getParamsHashMap();

    void setDeviceId() {
        this.device = "androidsdk_" + Settings.Secure.getString(PaystackSdk.applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

}
