package co.paystack.android;

import co.paystack.android.api.model.TransactionApiResponse;

public class Transaction {
    private String id;
    private String reference;

    public static Transaction EMPTY_TRANSACTION = new Transaction();

    void loadFromResponse(TransactionApiResponse t) {
        if (t.hasValidReferenceAndTrans()) {
            this.reference = t.reference;
            this.id = t.trans;
        }
    }

    String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setId(String id) {
        this.id = id;
    }

    boolean hasStartedOnServer() {
        return (reference != null) && (id != null);
    }

}
