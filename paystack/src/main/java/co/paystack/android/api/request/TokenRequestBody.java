package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

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


    private TokenRequestBody() {
  }

    public TokenRequestBody(String clientData, String publicKey) {
    this.clientData = clientData;
        this.publicKey = publicKey;
  }

    @Override
    public HashMap<String, String> getParamsHashMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(FIELD_PUBLIC_KEY, publicKey);
        params.put(FIELD_CLIENT_DATA, clientData);
        return params;
    }
}
