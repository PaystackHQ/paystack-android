package co.paystack.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import co.paystack.android.api.ApiClient;
import co.paystack.android.api.model.TransactionApiResponse;
import co.paystack.android.api.request.ChargeRequestBody;
import co.paystack.android.api.request.ValidateRequestBody;
import co.paystack.android.api.service.ApiService;
import co.paystack.android.exceptions.AuthenticationException;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.ValidateException;
import co.paystack.android.model.Charge;
import co.paystack.android.ui.OtpActivity;
import co.paystack.android.ui.OtpSingleton;
import co.paystack.android.ui.PinActivity;
import co.paystack.android.ui.PinSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by i on 24/08/2016.
 */
public class TransactionManager {

    private static final String LOG_TAG = TransactionManager.class.getSimpleName();
    private final Activity activity;
    private final Charge charge;
    private final Paystack.TransactionCallback transactionCallback;
    private final PinSingleton psi = PinSingleton.getInstance();
    private final OtpSingleton osi = OtpSingleton.getInstance();
    private ChargeRequestBody chargeRequestBody;
    private ValidateRequestBody validateRequestBody;
    private ApiService apiService;

    public TransactionManager(Activity activity, Charge charge, Paystack.TransactionCallback transactionCallback) {
        this.activity = activity;
        this.charge = charge;
        this.transactionCallback = transactionCallback;
    }

    public void initiate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (apiService == null) {
            apiService = new ApiClient().getApiService();
        }
        if (chargeRequestBody == null) {
            chargeRequestBody = new ChargeRequestBody(charge);
        }
        if (validateRequestBody == null) {
            validateRequestBody = new ValidateRequestBody();
        }
    }

    public void performTransaction() {
        try {

            initiate();

            //request token from paystack server
            initiateChargeOnServer();

        } catch (CardException
                | KeyManagementException
                | NoSuchAlgorithmException
                | AuthenticationException
                | KeyStoreException ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            if (transactionCallback != null) {
                transactionCallback.onError(ce);
            }
        }

    }

    public void validate() {
        try {

            //request token from paystack server
            validateChargeOnServer();

        } catch (CardException
                | KeyManagementException
                | NoSuchAlgorithmException
                | AuthenticationException
                | KeyStoreException ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            if (transactionCallback != null) {
                transactionCallback.onError(ce);
            }
        }

    }

    private void validateChargeOnServer() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        HashMap<String, String> params = validateRequestBody.getParamsHashMap();

        Call<TransactionApiResponse> call = apiService.validateCharge(params);
        call.enqueue(new Callback<TransactionApiResponse>() {
            @Override
            public void onResponse(Call<TransactionApiResponse> call, Response<TransactionApiResponse> response) {
                TransactionApiResponse transactionApiResponse = response.body();
                if (transactionApiResponse != null) {
                    //check for status...if 0 return an error with the message
                    if (transactionApiResponse.status == 0) {
                        //throw an error
                        transactionCallback.onError(new ValidateException(transactionApiResponse.message));
                        return;
                    } else if (transactionApiResponse.status == 1) {
                        // needs pin
                        // show dialog
                        transactionCallback.onValidate(transactionApiResponse.getTransaction());
                        return;
                    }
                }
                transactionCallback.onError(new RuntimeException("Unknown server response"));
            }

            @Override
            public void onFailure(Call<TransactionApiResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                transactionCallback.onError(t);
            }


        });
    }

    private void initiateChargeOnServer() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        HashMap<String, String> params = chargeRequestBody.getParamsHashMap();

        Call<TransactionApiResponse> call = apiService.charge(params);
        call.enqueue(new Callback<TransactionApiResponse>() {
            @Override
            public void onResponse(Call<TransactionApiResponse> call, Response<TransactionApiResponse> response) {
                TransactionApiResponse transactionApiResponse = response.body();
                if (transactionApiResponse != null) {
                    //check for status...if 0 return an error with the message
                    if (transactionApiResponse.status == 0) {
                        //throw an error
                        transactionCallback.onError(new ChargeException(transactionApiResponse.message));
                        return;
                    } else if (transactionApiResponse.status == 2) {
                        // needs pin
                        // show dialog
                        new PinAsyncTask().execute();
                        return;
                    } else if (transactionApiResponse.status == 3 && transactionApiResponse.hasValidReferenceAndTrans()) {
                        // needs otp
                        // show dialog
                        transactionCallback.beforeValidate(transactionApiResponse.getTransaction());
                        validateRequestBody.setTrans(transactionApiResponse.trans);
                        osi.setOtpMessage(transactionApiResponse.message);
                        new OtpAsyncTask().execute();
                        return;
                    } else if (transactionApiResponse.hasValidReferenceAndTrans()) {
                        transactionCallback.onValidate(transactionApiResponse.getTransaction());
                        return;
                    }
                }
                transactionCallback.onError(new RuntimeException("Unknown server response"));
            }

            @Override
            public void onFailure(Call<TransactionApiResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                transactionCallback.onError(t);
            }


        });
    }

    private class PinAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Intent i = new Intent(activity, PinActivity.class);
            activity.startActivity(i);


            synchronized (psi) {
                try {
                    psi.wait();
                } catch (InterruptedException e) {
                    transactionCallback.onError(new Exception("PIN entry Interrupted"));
                }
            }

            return psi.getPin();
        }

        @Override
        protected void onPostExecute(String pin) {
            super.onPostExecute(pin);
            if (pin != null && (4 == pin.length())) {
                chargeRequestBody.addPin(pin);
                TransactionManager.this.performTransaction();
            } else {
                transactionCallback.onError(new Exception("PIN must be exactly 4 digits"));
            }
        }
    }

    private class OtpAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Intent i = new Intent(activity, OtpActivity.class);
            activity.startActivity(i);


            synchronized (osi) {
                try {
                    osi.wait();
                } catch (InterruptedException e) {
                    transactionCallback.onError(new Exception("PIN entry Interrupted"));
                }
            }

            return osi.getOtp();
        }

        @Override
        protected void onPostExecute(String otp) {
            super.onPostExecute(otp);
            if (otp != null) {
                validateRequestBody.setToken(otp);
                TransactionManager.this.validate();
            } else {
                transactionCallback.onError(new Exception("PIN must be exactly 4 digits"));
            }
        }
    }

}