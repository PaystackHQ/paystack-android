package co.paystack.android.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * A transaction request
 */
public class TransactionRequestBody {

  public static final String FIELD_MERCHANT_ID = "merchantid";
  public static final String FIELD_SECRET_KEY = "secretkey";
  public static final String FIELD_TRX_REF = "trxref";
  public static final String FIELD_EMAIL = "email";
  public static final String FIELD_AMOUNT = "amount";
  public static final String FIELD_TOKEN = "token";
  public static final String FIELD_PLAN = "plan";
  public static final String FIELD_QUANTITY = "quantity";
  public static final String FIELD_COUPON = "coupon";

  @SerializedName(FIELD_MERCHANT_ID)
  public String merchantId;

  @SerializedName(FIELD_SECRET_KEY)
  public String secretKey;

  @SerializedName(FIELD_TRX_REF)
  public String transactionRef;

  @SerializedName(FIELD_EMAIL)
  public String email;

  @SerializedName(FIELD_AMOUNT)
  public String amount;

  @SerializedName(FIELD_TOKEN)
  public String token;

  @SerializedName(FIELD_PLAN)
  public String plan;

  @SerializedName(FIELD_QUANTITY)
  public String quantity;

  @SerializedName(FIELD_COUPON)
  public String coupon;

}
