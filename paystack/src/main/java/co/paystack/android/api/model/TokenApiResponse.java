package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by   {androidsupport@paystack.co} on 9/17/15.
 */
@Parcel
public class TokenApiResponse extends ApiResponse implements Serializable{

    @SerializedName("token")
    public String token;

    @SerializedName("last4")
    public String last4;
}
