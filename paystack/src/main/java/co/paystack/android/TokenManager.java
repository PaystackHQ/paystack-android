package co.paystack.android;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TokenManager class, to perform action token creation. You don't have to use this class.
 *
 * @author {androidsupport@paystack.co} on 9/17/15.
 */
public class TokenManager implements Paystack.TokenCreator {

    private static final String LOG_TAG = TokenManager.class.getSimpleName();

    @Override
    public void create(final Card card, String publicKey, final Paystack.TokenCallback tokenCallback, Executor executor) {
        try {
            //concatenate card fields
            String cardString = StringUtils.concatenateCardFields(card);

            //encrypt
            final String encCardString = Crypto.encrypt(cardString);

            //create tokenRequestBody
            final TokenRequestBody tokenRequestBody = new TokenRequestBody(encCardString, publicKey);

            //request token from paystack server
            createServerSideToken(tokenRequestBody, tokenCallback);

        } catch (CardException
                | KeyManagementException
                | NoSuchAlgorithmException
                | AuthenticationException
                | KeyStoreException ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            if (tokenCallback != null) {
                tokenCallback.onError(ce);
            }
        }

    }


    private void createServerSideToken(TokenRequestBody tokenRequestBody, final Paystack.TokenCallback tokenCallback) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        //call retrofit api service
        ApiService apiService = new ApiClient().getApiService();

        HashMap<String, String> params = new HashMap<>();
        params.put(TokenRequestBody.FIELD_PUBLIC_KEY, tokenRequestBody.publicKey);
        params.put(TokenRequestBody.FIELD_CLIENT_DATA, tokenRequestBody.clientData);

        Call<TokenApiResponse> call = apiService.createToken(params);
        call.enqueue(new Callback<TokenApiResponse>() {
            /**
             * Invoked for a received HTTP response.
             * <p/>

             * @param call     - the call enqueueing this callback
             * @param response - response from the server after call is made
             */
            @Override
            public void onResponse(Call<TokenApiResponse> call, Response<TokenApiResponse> response) {
                TokenApiResponse tokenApiResponse = response.body();
                if (tokenApiResponse != null) {
                    //check for status...if 0 return an error with the message
                    if (tokenApiResponse.status == 0) {
                        //throw an error
                        tokenCallback.onError(new TokenException(tokenApiResponse.message));
                    } else {
                        Token token = new Token();
                        token.token = tokenApiResponse.token;
                        token.last4 = tokenApiResponse.last4;

                        tokenCallback.onCreate(token);
                    }
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call - call that enqueued this callback
             * @param t    - the error or exception that caused the failure
             */
            @Override
            public void onFailure(Call<TokenApiResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                // Don't want to change public method signature yet
                // this is meant to be a minor revision
                // TODO: in a major revision, make TokenCallback.onError use throwable directly
                if (t instanceof Exception) {
                    tokenCallback.onError((Exception) t);
                } else {
                    t.printStackTrace();
                    tokenCallback.onError(new Exception(t.getMessage(), t));
                }

            }

        });
    }

}