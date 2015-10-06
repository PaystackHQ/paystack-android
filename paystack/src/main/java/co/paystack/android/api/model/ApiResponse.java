package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Segun Famisa {segunfamisa@gmail.com} on 9/20/15.
 */
public class ApiResponse extends BaseApiModel{

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

}
