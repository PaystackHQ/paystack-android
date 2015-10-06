package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Segun Famisa {segunfamisa@gmail.com} on 9/18/15.
 */
public class TokenRequestBody {

    /**
     * Value for encrypted & concatenated card details
     */
    @SerializedName("clientdata")
    public String clientdata;

    /**
     * Value for cardtype
     */
    @SerializedName("cardtype")
    public String cardtype;

    public TokenRequestBody(){}

    public TokenRequestBody(String clientData, String cardType){
        this.clientdata = clientData;
        this.cardtype = cardType;
    }


    public static final String FIELD_CLIENTDATA = "clientdata";
    public static final String FIELD_CARDTYPE = "cardtype";
}
