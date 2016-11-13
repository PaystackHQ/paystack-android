package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * An API response always includes a status and a message
 */
public class ApiResponse extends BaseApiModel {

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

}
