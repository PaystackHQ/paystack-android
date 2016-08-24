package co.paystack.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.Executor;

import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Card;
import co.paystack.android.model.PaystackModel;
import co.paystack.android.model.Token;
import co.paystack.android.ui.PinActivity;
import co.paystack.android.ui.PinSingleton;
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
    private String publishableKey;
    private Card card;

    /**
     * Constructor.
     */
    protected Paystack() throws PaystackSdkNotInitializedException {
        //validate sdk initialized
        Utils.Validate.validateSdkInitialized();
    }

    protected Paystack(String publishableKey) throws AuthenticationException {
        setPublishableKey(publishableKey);
    }

    /**
     * Sets the publishable key
     *
     * @param publishableKey - App Developer's publishable key
     */
    public void setPublishableKey(String publishableKey) throws AuthenticationException {
        //validate the publishable key
        validatePublishableKey(publishableKey);
        this.publishableKey = publishableKey;
    }

    private void validatePublishableKey(String publishableKey) throws AuthenticationException {
        //check for null value, and length and startswith pk_
        if (publishableKey == null || publishableKey.length() < 1 || !publishableKey.startsWith("pk_")) {
            throw new AuthenticationException("Invalid publishable key. To create a token, " +
                    "you must use a valid publishable key.\nEnsure that you have set a publishable key." +
                    "\nCheck http://paystack.co for more");
        }

    }

    private TokenCallback tokenCallback;

    protected void setTokenCallBack(final TokenCallback callback) {
        this.tokenCallback = callback;
    }


    /**
     * Method to create token for the transaction, assumes the publishable key has been previously set.
     *
     * @param card          - a card whose token we want to create
     * @param tokenCallback - a callback to execute after getting the token
     * @throws AuthenticationException if publishable key hasn't been set.
     */
    public void createToken(Card card, Activity activity, TokenCallback tokenCallback) {
        this.setTokenCallBack(tokenCallback);
        this.setCard(card);
        new ParentAsyncTask().execute(activity);

    }

    public void setCard(Card card) {
        this.card = card;
    }

    private class ParentAsyncTask extends AsyncTask<Activity, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Activity... params) {
            // do what you need and if you decide to stop this activity and wait for the sub-activity, do this
            Intent i = new Intent(params[0], PinActivity.class);
            params[0].startActivity(i);
            PinSingleton si = PinSingleton.getInstance();
            //si.setOtpMessage("Hullabloo");
            synchronized (si) {
                try {
                    si.wait();
                } catch (InterruptedException e) {
                    tokenCallback.onError(new Exception("PIN entry Interrupted"));
                }
            }
            Log.d("HAHAHAHAHA", "Yes!! I waited and got token: " + si.getPin());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                createToken(card, publishableKey, tokenCallback);
            } else {
                tokenCallback.onError(new Exception("UNxepectd"));
            }
        }
    }

    /**
     * Method to create token for the transaction
     *
     * @param card           - a card
     * @param publishableKey - the key provided by App Developer
     * @param tokenCallback  - a callback to execute after getting the token
     */
    public void createToken(Card card, String publishableKey, TokenCallback tokenCallback) {
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

            //validate publishable key
            validatePublishableKey(publishableKey);

            //null check for tokenCallback
            if (tokenCallback == null) {
                throw new RuntimeException("Required parameter, tokenCallback cannot be null");
            }


            //do actual create token.
            tokenManager.create(card, publishableKey, tokenCallback, null);
        } catch (AuthenticationException | CardException ae) {
            assert tokenCallback != null;
            tokenCallback.onError(ae);
        }
    }

    interface TokenCreator {
        void create(Card card, String publishableKey, TokenCallback tokenCallback, Executor executor);
    }

    public interface TokenCallback {
        void onCreate(Token token);

        void onError(Exception error);
    }

}
