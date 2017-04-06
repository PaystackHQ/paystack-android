package co.paystack.android.api.model;

import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 3DS would give a redirect url at which we can conclude payment
 */
public class TransactionApiResponse extends ApiResponse implements Serializable {

    @SerializedName("reference")
    public String reference;

    @SerializedName("trans")
    public String trans;

    @SerializedName("auth")
    public String auth;

    @SerializedName("otpmessage")
    public String otpmessage;

    public static TransactionApiResponse unknownServerResponse() {
        TransactionApiResponse t = new TransactionApiResponse();
        t.status = "0";
        t.message = "Unknown server response";
        return t;
    }

    public static TransactionApiResponse fromJsonString(String jsonString) {
        try {
            return new Gson().fromJson(jsonString, TransactionApiResponse.class);
        } catch (Exception e) {
            return TransactionApiResponse.unknownServerResponse();
        }
    }

    public boolean hasValidReferenceAndTrans() {
        return (reference != null) && (trans != null);
    }

    public boolean hasValidUrl() {
        return otpmessage != null && URLUtil.isValidUrl(otpmessage);
    }

    public boolean hasValidOtpMessage() {
        return otpmessage != null;
    }

    public boolean hasValidAuth() {
        return auth != null;
    }

}
