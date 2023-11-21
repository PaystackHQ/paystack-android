package co.paystack.android;

import static co.paystack.android.ConstantsKt.DEPRECATION_MESSAGE;

import android.app.Activity;

import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Charge;
import co.paystack.android.model.PaystackModel;
import co.paystack.android.utils.Utils;
import kotlin.Deprecated;

/**
 * This is the Paystack model class.\n
 * <br>
 * Try not to use this class directly.
 * Instead, access the functionalities of this class via the {@link PaystackSdk}
 *
 * @author {androidsupport@paystack.co} on 9/16/15.
 */
@Deprecated(message = DEPRECATION_MESSAGE)
public class Paystack extends PaystackModel {

    private String publicKey;

    /**
     * Constructor.
     */
    protected Paystack() throws PaystackSdkNotInitializedException {
        //validate sdk initialized
        Utils.Validate.validateSdkInitialized();
    }

    protected Paystack(String publicKey) throws AuthenticationException {
        setPublicKey(publicKey);
    }

    /**
     * Sets the public key
     *
     * @param publicKey - App Developer's public key
     */
    private void setPublicKey(String publicKey) throws AuthenticationException {
        //validate the public key
        validatePublicKey(publicKey);
        this.publicKey = publicKey;
    }

    private void validatePublicKey(String publicKey) throws AuthenticationException {
        //check for null value, and length and startswith pk_
        if (publicKey == null || publicKey.length() < 1 || !publicKey.startsWith("pk_")) {
            throw new AuthenticationException("Invalid public key. To create a token, " +
                    "you must use a valid public key.\nEnsure that you have set a public key." +
                    "\nCheck http://paystack.co for more");
        }

    }

    @Deprecated(message = DEPRECATION_MESSAGE)
    void chargeCard(Activity activity, Charge charge, TransactionCallback transactionCallback) {
        chargeCard(activity, charge, publicKey, transactionCallback);
    }


    private void chargeCard(Activity activity, Charge charge, String publicKey, TransactionCallback transactionCallback) {
        //check for the needed data, if absent, send an exception through the tokenCallback;
        try {
            //validate public key
            validatePublicKey(publicKey);

            TransactionManager transactionManager = PaystackSdkComponentKt.sdkComponent()
                    .getTransactionManagerFactory()
                    .create();

            transactionManager.chargeCard(activity, PaystackSdk.getPublicKey(), charge, transactionCallback);

        } catch (Exception ae) {
            assert transactionCallback != null;
            transactionCallback.onError(ae, null);
        }
    }

    private interface BaseCallback {
    }

    @Deprecated(message = DEPRECATION_MESSAGE)
    public interface TransactionCallback extends BaseCallback {
        @Deprecated(message = DEPRECATION_MESSAGE)
        void onSuccess(Transaction transaction);

        @Deprecated(message = DEPRECATION_MESSAGE)
        void beforeValidate(Transaction transaction);

        @Deprecated(message = DEPRECATION_MESSAGE)
        void showLoading(Boolean isProcessing);

        @Deprecated(message = DEPRECATION_MESSAGE)
        void onError(Throwable error, Transaction transaction);
    }

}
