package co.paystack.android;

import java.util.HashMap;
import java.util.concurrent.Executor;

import co.paystack.android.api.ApiClient;
import co.paystack.android.api.model.TokenApiResponse;
import co.paystack.android.api.request.TokenRequestBody;
import co.paystack.android.api.service.ApiService;
import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.TokenException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Token;
import co.paystack.android.utils.Crypto;
import co.paystack.android.utils.StringUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * TokenManager class, to perform action token creation. You don't have to use this class.
 *
 * @author Segun Famisa {segunfamisa@gmail.com} on 9/17/15.
 */
public class TokenManager implements Paystack.TokenCreator {
    private static final String CARD_CONCATENATOR = "*";

    private static final String TAG = "TokenManager";

    @Override
    public void create(final Card card, String publishableKey, final Paystack.TokenCallback tokenCallback, Executor executor) {
        try{
            //concatenate card fields
            String cardString = concatenateCardFields(card);

            //encrypt
            final String encCardString = Crypto.encrypt(cardString, publishableKey);

            //create tokenRequestBody
            final TokenRequestBody tokenRequestBody = new TokenRequestBody(encCardString, card.getType());

            //request token from paystack server
            createServerSideToken(tokenRequestBody, tokenCallback);

        }
        catch (CardException ce){
            if(tokenCallback != null)
                tokenCallback.onError(ce);
        }
        catch (AuthenticationException ae) {
            if(tokenCallback != null){
                tokenCallback.onError(ae);
            }
        }

    }


    private void createServerSideToken(TokenRequestBody tokenRequestBody, final Paystack.TokenCallback tokenCallback){
        //call retrofit api service
        ApiService apiService = new ApiClient().getApiService();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TokenRequestBody.FIELD_CARDTYPE, tokenRequestBody.cardtype);
        params.put(TokenRequestBody.FIELD_CLIENTDATA, tokenRequestBody.clientdata);

        apiService.createToken(params, new Callback<TokenApiResponse>() {
            @Override
            public void success(TokenApiResponse tokenApiResponse, Response response) {
                if(tokenApiResponse != null){
                    //check for status...if 0 return an error with the message
                    if(tokenApiResponse.status == 0){
                        //throw an error
                        tokenCallback.onError(new TokenException(tokenApiResponse.message));
                    }
                    else{
                        Token token = new Token();
                        token.token = tokenApiResponse.token;
                        token.last4 = tokenApiResponse.last4;

                        tokenCallback.onCreate(token);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tokenCallback.onError(error);
            }
        });
    }

    private String concatenateCardFields(Card card) throws CardException{
        if(card == null){
            throw new CardException("Card cannot be null");
        }

        String number = StringUtils.nullify(card.getNumber());
        String cvc = StringUtils.nullify(card.getCvc());
        int expiryMonth = card.getExpiryMonth();
        int expiryYear = card.getExpiryYear();

        String cardString = null;
        String[] cardFields = {number, cvc, expiryMonth+"", expiryYear+""};

        if(!StringUtils.isEmpty(number)){
            for(int i=0; i<cardFields.length; i++){
                if(i == 0 && cardFields.length > 1)
                    cardString = cardFields[i] + CARD_CONCATENATOR;
                else if(i == cardFields.length - 1)
                    cardString += cardFields[i];
                else
                    cardString = cardString + cardFields[i] + CARD_CONCATENATOR;
            }
            return cardString;
        }

        else{
            throw new CardException("Invalid card details: Card number is empty or null");
        }
    }
}
