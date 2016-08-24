package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 3DS would give a redirect url at which we can conclude payment
 */
public class ValidateApiResponse extends ApiResponse implements Serializable {

    @SerializedName("redirecturl")
    public String redirecturl;

}
