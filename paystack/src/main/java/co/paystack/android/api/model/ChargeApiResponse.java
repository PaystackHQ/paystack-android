package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Transaction ID is required in order to validate charge
 */
public class ChargeApiResponse extends ApiResponse implements Serializable {

    @SerializedName("reference")
    public String reference;

    @SerializedName("trans")
    public String trans;

}