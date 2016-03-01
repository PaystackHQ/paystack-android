package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * A token request
 */
public class TokenRequestBody {

  public static final String FIELD_CLIENT_DATA = "clientdata";
  public static final String FIELD_PUBLISHABLE_KEY = "publishablekey";
  /**
   * Value for encrypted and concatenated card details
   */
  @SerializedName("clientdata")
  public String clientData;
  /**
   * Value for cardtype
   */
  @SerializedName("publishablekey")
  public String publishableKey;


  public TokenRequestBody() {
  }

  public TokenRequestBody(String clientData, String publishableKey) {
    this.clientData = clientData;
    this.publishableKey = publishableKey;
  }
}
