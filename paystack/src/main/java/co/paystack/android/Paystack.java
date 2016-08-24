package co.paystack.android;

import android.app.Activity;

import java.util.concurrent.Executor;

import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import co.paystack.android.model.PaystackModel;
import co.paystack.android.model.Token;
import co.paystack.android.model.Transaction;
import co.paystack.android.utils.Utils;

/**
 * This is the Paystack model class.\n
 * <br>
 * Try not to use this class directly.
 * Instead, access the functionalities of this class via the {@link PaystackSdk}
 *
 * @author {androidsupport@paystack.co} on 9/16/15.
 */
public class Paystack extends PaystackModel {

    private static final TokenManager tokenManager = new TokenManager();
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
    public void setPublicKey(String publicKey) throws AuthenticationException {
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

    /**
     * Method to create token for the transaction, assumes the public key has been previously set.
     *
     * @param card          - a card whose token we want to create
     * @param tokenCallback - a callback to execute after getting the token
     * @throws AuthenticationException if public key hasn't been set.
     */
    public void createToken(Card card, TokenCallback tokenCallback) {
        createToken(card, publicKey, tokenCallback);
    }

    public void chargeCard(Activity activity, Charge charge, TransactionCallback transactionCallback) {
        chargeCard(activity, charge, publicKey, transactionCallback);
    }

    /**
     * Method to create token for the transaction
     *
     * @param card           - a card
     * @param publicKey - the key provided by App Developer
     * @param tokenCallback  - a callback to execute after getting the token
     */
    public void createToken(Card card, String publicKey, TokenCallback tokenCallback) {
        //check for the needed data, if absent, send an exception through the tokenCallback;
        try {
            //null check for card
            if (card == null) {
                throw new RuntimeException("Required parameter: Card cannot be null");
            }
            //validate card
            if (!card.isValid()) {
                throw new CardException("Invalid parameter: Card not valid");
            }

            //validate public key
            validatePublicKey(publicKey);

            //null check for tokenCallback
            if (tokenCallback == null) {
                throw new RuntimeException("Required parameter, tokenCallback cannot be null");
            }


            //do actual create token.
            tokenManager.create(card, publicKey, tokenCallback, null);
        } catch (AuthenticationException | CardException ae) {
            assert tokenCallback != null;
            tokenCallback.onError(ae);
        }
    }

    public void chargeCard(Activity activity, Charge charge, String publicKey, TransactionCallback transactionCallback) {
        //check for the needed data, if absent, send an exception through the tokenCallback;
        try {
            //null check for card
            if(charge == null){
                throw new RuntimeException("Required parameter: Charge cannot be null");
            }

            if (charge.getAmount() < 5000) {
                throw new RuntimeException("Required parameter: Amount cannot be less than 5000 (50 naira)");
            }

            if (charge.getCard() == null) {
                throw new RuntimeException("Required parameter: Card cannot be null");
            }
            //validate card
            if (!charge.getCard().isValid()) {
                throw new CardException("Invalid parameter: Card not valid");
            }

            //validate public key
            validatePublicKey(publicKey);

            //null check for tokenCallback
            if (transactionCallback == null) {
                throw new RuntimeException("Required parameter, tokenCallback cannot be null");
            }

            TransactionManager transactionManager = new TransactionManager(activity, charge, transactionCallback);

            transactionManager.initiate();

        } catch (AuthenticationException | CardException ae) {
            assert transactionCallback != null;
            transactionCallback.onError(ae);
        }
    }

    interface TokenCreator {
        void create(Card card, String publicKey, TokenCallback tokenCallback, Executor executor);
    }

    public interface BaseCallback {
        void onError(Exception error);
    }

    public interface TokenCallback extends BaseCallback{
        void onCreate(Token token);

    }

    public interface TransactionCallback {
        void onValidate(Transaction transaction);

        void beforeValidate(Transaction transaction);

        void onError(Throwable error);
    }

}