package co.paystack.android.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import co.paystack.android.model.Transaction;

/**
 * 3DS would give a redirect url at which we can conclude payment
 */
public class TransactionApiResponse extends ApiResponse implements Serializable {

    @SerializedName("reference")
    public String reference;

    @SerializedName("trans")
    public String trans;

    @SerializedName("redirecturl")
    public String redirecturl;

    public boolean hasValidReferenceAndTrans() {
        return trans != null && reference != null;
    }

    public Transaction getTransaction() {
        return new Transaction(reference);
    }

}
