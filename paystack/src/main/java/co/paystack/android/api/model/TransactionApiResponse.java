package co.paystack.android.api.model;

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

    @SerializedName("redirecturl")
    public String redirecturl;

}
