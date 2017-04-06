package co.paystack.android;

import co.paystack.android.api.model.TransactionApiResponse;

public class Transaction {
    private String id;
    private String reference;

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

    boolean hasStartedOnServer() {
        return (reference != null) && (id != null);
    }

}
