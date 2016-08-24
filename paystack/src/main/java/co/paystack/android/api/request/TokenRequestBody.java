package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * A token request
 */
public class TokenRequestBody extends BaseRequestBody {

  public static final String FIELD_CLIENT_DATA = "clientdata";
    public static final String FIELD_PUBLIC_KEY = "public_key";
  /**
   * Value for encrypted and concatenated card details
   */
  @SerializedName("clientdata")
  public String clientData;
  /**
   * Value for cardtype
   */
  @SerializedName("public_key")
  public String publicKey;


  public TokenRequestBody() {
  }

    public TokenRequestBody(String clientData, String publicKey) {
    this.clientData = clientData;
        this.publicKey = publicKey;
  }
}
