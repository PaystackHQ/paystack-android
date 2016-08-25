package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * A Response received from the API when a Token Request is made
 */
public class TokenApiResponse extends ApiResponse implements Serializable {

    @SerializedName("token")
    public String token;

    @SerializedName("last4")
    public String last4;
}
