package co.paystack.android;

import java.util.concurrent.Executor;

import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Card;
import co.paystack.android.model.PaystackModel;
import co.paystack.android.model.Token;
import co.paystack.android.utils.Utils;

/**
 * This is the Paystack model class.\n
 *
 * Try not to use this class directly.
 * Instead, access the functionalities of this class via the {@link PaystackSdk}
 *
 * @author Segun Famisa {segunfamisa@gmail.com} on 9/16/15.
 */
public class Paystack extends PaystackModel {

    private String publishableKey;

    private static final TokenManager tokenManager = new TokenManager();

    /**
     * Constructor.
     */
    protected Paystack() throws PaystackSdkNotInitializedException {
        //validate sdk initialized
        Utils.Validate.validateSdkInitialized();
    }

    protected Paystack(String publishableKey) throws AuthenticationException{
        setPublishableKey(publishableKey);
    }

    /**
     * Sets the publishable key
     * @param publishableKey
     */
    public void setPublishableKey(String publishableKey) throws AuthenticationException{
        //validate the publishable key
        validatePublishableKey(publishableKey);
        this.publishableKey = publishableKey;
    }

    private void validatePublishableKey(String publishableKey) throws AuthenticationException{
        //check for null value, and length
        if(publishableKey == null || publishableKey.length() < 1){
            throw new AuthenticationException("Invalid publishable key. To create a token, " +
                    "you must use a valid publishable key.\nEnsure that you have set a publishable key."+
                    "\nCheck http://paystack.co for more");
        }

    }

    /**
     * Method to create token for the transaction, assumes the publishable key has been previously set.
     *
     * @param card
     * @param tokenCallback
     *
     * @throws AuthenticationException if publishable key hasn't been set.
     */
    public void createToken(Card card, TokenCallback tokenCallback){
        createToken(card, publishableKey, tokenCallback);
    }

    /**
     * Method to create token for for the transaction
     * @param card
     * @param publishableKey
     * @param tokenCallback
     */
    public void createToken(Card card, String publishableKey, TokenCallback tokenCallback){
        //check for the needed data, if absent, a stripe exception through the tokenCallback;
        try{
            //null check for card
            if(card == null){
                throw new RuntimeException("Required parameter: Card cannot be null");
            }
            //validate card
            if(!card.isValid()){
                throw new CardException("Invalid parameter: Card not valid");
            }

            //validate publishable key
            validatePublishableKey(publishableKey);

            //null check for tokenCallback
            if(tokenCallback == null){
                throw new RuntimeException("Required parameter, tokenCallback cannot be null");
            }


            //do actual create token.
            tokenManager.create(card, publishableKey, tokenCallback, null);
        }
        catch (AuthenticationException ae){
            tokenCallback.onError(ae);
        }
        catch (CardException ce){
            tokenCallback.onError(ce);
        }
    }

    interface TokenCreator{
         void create(Card card, String publishableKey, TokenCallback tokenCallback, Executor executor);
    }

    public interface TokenCallback{
        public void onCreate(Token token);
        public void onError(Exception error);
    }

}
