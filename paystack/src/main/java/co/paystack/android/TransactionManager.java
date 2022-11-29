package co.paystack.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import co.paystack.android.api.model.TransactionApiResponse;
import co.paystack.android.api.request.ChargeRequestBody;
import co.paystack.android.api.request.ValidateRequestBody;
import co.paystack.android.api.service.ApiService;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.ChargeException;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.exceptions.ProcessingException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import co.paystack.android.ui.AddressHolder;
import co.paystack.android.ui.AddressHolder.Address;
import co.paystack.android.ui.AddressVerificationActivity;
import co.paystack.android.ui.AuthActivity;
import co.paystack.android.ui.AuthSingleton;
import co.paystack.android.ui.CardActivity;
import co.paystack.android.ui.CardSingleton;
import co.paystack.android.ui.OtpActivity;
import co.paystack.android.ui.OtpSingleton;
import co.paystack.android.ui.PinActivity;
import co.paystack.android.ui.PinSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class TransactionManager {

    private static final String LOG_TAG = TransactionManager.class.getSimpleName();
    private static boolean PROCESSING = false;


    private Charge charge;
    private Activity activity;
    private Transaction transaction;
    private Paystack.TransactionCallback transactionCallback;


    private final CardSingleton cns = CardSingleton.getInstance();
    private final PinSingleton psi = PinSingleton.getInstance();
    private final OtpSingleton osi = OtpSingleton.getInstance();
    private final AuthSingleton asi = AuthSingleton.getInstance();
    private final AddressHolder addressHolder = AddressHolder.getInstance();


    private ChargeRequestBody chargeRequestBody;
    private ValidateRequestBody validateRequestBody;
    private ApiService apiService;

    private int invalidDataSentRetries = 0;

    private final Callback<TransactionApiResponse> serverCallback = new Callback<TransactionApiResponse>() {
        @Override
        public void onResponse(Call<TransactionApiResponse> call, Response<TransactionApiResponse> response) {
            handleApiResponse(response.body());
            transactionCallback.showLoading(false);
        }

        @Override
        public void onFailure(Call<TransactionApiResponse> call, Throwable t) {
            Log.e(LOG_TAG, t.getMessage());
            notifyProcessingError(t);
        }
    };

    TransactionManager(ApiService apiService) {
        this.apiService = apiService;
    }

    private void initiate() throws ProcessingException {
        if (TransactionManager.PROCESSING) {
            throw new ProcessingException();
        }
        setProcessingOn();
        String deviceId = "androidsdk_" + Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        chargeRequestBody = new ChargeRequestBody(charge, deviceId);
        validateRequestBody = new ValidateRequestBody(deviceId);
    }

    void chargeCard(Activity activity, Charge charge, Paystack.TransactionCallback transactionCallback) {
        if (BuildConfig.DEBUG && (activity == null)) {
            throw new AssertionError("activity must not be null");
        }
        if (BuildConfig.DEBUG && (charge == null)) {
            throw new AssertionError("charge must not be null");
        }
        if (BuildConfig.DEBUG && (charge.getCard() == null)) {
            throw new AssertionError("please add a card to the charge before calling chargeCard");
        }
        if (BuildConfig.DEBUG && (transactionCallback == null)) {
            throw new AssertionError("transactionCallback must not be null");
        }

        this.activity = activity;
        this.charge = charge;
        this.transactionCallback = transactionCallback;
        this.transaction = new Transaction();

        charge();
    }

    private void charge() {
        try {
            if (charge.getCard() == null || !charge.getCard().isValid()) {
                final CardSingleton si = CardSingleton.getInstance();
                synchronized (si) {
                    si.setCard(charge.getCard());
                }
                new CardAsyncTask().execute();
            } else {
                initiate();
                sendChargeToServer();
            }
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            if (!(ce instanceof ProcessingException)) {
                setProcessingOff();
            }
            transactionCallback.onError(ce, transaction);
        }
    }

    private void sendChargeToServer() {
        try {
            initiateChargeOnServer();
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            notifyProcessingError(ce);
        }
    }

    private void validate() {
        try {
            validateChargeOnServer();
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            notifyProcessingError(ce);
        }

    }

    private void reQuery() {
        try {
            reQueryChargeOnServer();
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            notifyProcessingError(ce);
        }

    }


    private void chargeWithAvs(Address address) {
        HashMap<String, String> fields = address.toHashMap();
        fields.put("trans", transaction.getId());
        try {
            this.transactionCallback.showLoading(true);
            Call<TransactionApiResponse> call = apiService.submitCardAddress(fields);
            call.enqueue(serverCallback);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            notifyProcessingError(e);
        }
    }

    private void validateChargeOnServer() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        this.transactionCallback.showLoading(true);
        HashMap<String, String> params = validateRequestBody.getParamsHashMap();
        Call<TransactionApiResponse> call = apiService.validateCharge(params);
        call.enqueue(serverCallback);
    }

    private void reQueryChargeOnServer() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        this.transactionCallback.showLoading(true);
        Call<TransactionApiResponse> call = apiService.requeryTransaction(transaction.getId());
        call.enqueue(serverCallback);
    }

    private void initiateChargeOnServer() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        this.transactionCallback.showLoading(true);
        Call<TransactionApiResponse> call = apiService.charge(chargeRequestBody.getParamsHashMap());
        call.enqueue(serverCallback);

    }

    private void handleApiResponse(TransactionApiResponse transactionApiResponse) {
        if (transactionApiResponse == null) {
            transactionApiResponse = TransactionApiResponse.unknownServerResponse();
        }

        // The AVS charge endpoint sends an "errors" object when address verification fails
        if (transactionApiResponse.hasErrors) {
            notifyProcessingError(new ChargeException(transactionApiResponse.message));
            return;
        }
        transaction.loadFromResponse(transactionApiResponse);

        if (transactionApiResponse.status.equalsIgnoreCase("1") || transactionApiResponse.status.equalsIgnoreCase("success")) {
            setProcessingOff();
            transactionCallback.onSuccess(transaction);
            return;
        }

        if (transactionApiResponse.status.equalsIgnoreCase("2") && transactionApiResponse.hasValidAuth() && transactionApiResponse.auth.equalsIgnoreCase("avs")) {
            new AddressVerificationAsyncTask().execute(transactionApiResponse.avsCountryCode);
            return;
        }

        if (transactionApiResponse.status.equalsIgnoreCase("2") || (transactionApiResponse.hasValidAuth() && (transactionApiResponse.auth.equalsIgnoreCase("pin")))) {
            new PinAsyncTask().execute();
            return;
        }

        if (transactionApiResponse.status.equalsIgnoreCase("3") && transactionApiResponse.hasValidReferenceAndTrans()) {
            transactionCallback.beforeValidate(transaction);
            validateRequestBody.setTrans(transactionApiResponse.trans);
            osi.setOtpMessage(transactionApiResponse.message);
            new OtpAsyncTask().execute();
            return;
        }

        if (transaction.hasStartedOnServer()) {
            if (transactionApiResponse.status.equalsIgnoreCase("requery")) {
                transactionCallback.beforeValidate(transaction);
                new CountDownTimer(5000, 5000) {
                    public void onFinish() {
                        reQuery();
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
                return;
            }
            if (transactionApiResponse.hasValidAuth() && (transactionApiResponse.auth.equalsIgnoreCase("3DS")) && transactionApiResponse.hasValidUrl()) {
                transactionCallback.beforeValidate(transaction);
                asi.setUrl(transactionApiResponse.otpmessage);
                new AuthAsyncTask().execute();
                return;
            }
            if (transactionApiResponse.hasValidAuth() && (transactionApiResponse.auth.equalsIgnoreCase("otp") || transactionApiResponse.auth.equalsIgnoreCase("phone")) && transactionApiResponse.hasValidOtpMessage()) {
                transactionCallback.beforeValidate(transaction);
                validateRequestBody.setTrans(transaction.getId());
                osi.setOtpMessage(transactionApiResponse.otpmessage);
                new OtpAsyncTask().execute();
                return;
            }
        }

        if (transactionApiResponse.status.equalsIgnoreCase("0") || transactionApiResponse.status.equalsIgnoreCase("error")) {
            //throw an error
            if (transactionApiResponse.message.equalsIgnoreCase("Invalid Data Sent") && invalidDataSentRetries < 3) {
                invalidDataSentRetries++;
                TransactionManager.this.sendChargeToServer();
                return;
            }
            if (transactionApiResponse.message.equalsIgnoreCase("Access code has expired")) {
                notifyProcessingError(new ExpiredAccessCodeException(transactionApiResponse.message));
                return;
            }
            notifyProcessingError(new ChargeException(transactionApiResponse.message));
            return;
        }

        notifyProcessingError(new RuntimeException("Unknown server response"));
    }

    private void notifyProcessingError(Throwable t) {
        setProcessingOff();
        transactionCallback.showLoading(false);
        transactionCallback.onError(t, transaction);
    }

    private void setProcessingOff() {
        TransactionManager.PROCESSING = false;
    }

    private void setProcessingOn() {
        TransactionManager.PROCESSING = true;
    }

    private class CardAsyncTask extends AsyncTask<Void, Void, Card> {

        @Override
        protected Card doInBackground(Void... params) {
            Intent i = new Intent(activity, CardActivity.class);
            activity.startActivity(i);

            synchronized (cns) {
                try {
                    cns.wait();
                } catch (InterruptedException e) {
                    notifyProcessingError(new Exception("Card entry Interrupted"));
                }
            }
            return cns.getCard();
        }

        @Override
        protected void onPostExecute(Card cns) {
            super.onPostExecute(cns);
            if (cns == null || !cns.isValid()) {
                notifyProcessingError(new CardException("Invalid card parameters"));
            } else {
                charge.setCard(cns);
                TransactionManager.this.charge();
            }
        }
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
                    notifyProcessingError(new Exception("PIN entry Interrupted"));
                }
            }

            return psi.getPin();
        }

        @Override
        protected void onPostExecute(String pin) {
            super.onPostExecute(pin);
            if (pin != null && (4 == pin.length())) {
                chargeRequestBody.addPin(pin);
                TransactionManager.this.sendChargeToServer();
            } else {
                notifyProcessingError(new Exception("PIN must be exactly 4 digits"));
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
                    notifyProcessingError(new Exception("OTP entry Interrupted"));
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
                notifyProcessingError(new Exception("You did not provide an OTP"));
            }
        }
    }


    private class AuthAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Intent i = new Intent(activity, AuthActivity.class);
            activity.startActivity(i);

            synchronized (asi) {
                try {
                    asi.wait();
                } catch (InterruptedException e) {
                    return asi.getResponseJson();
                }
            }

            return asi.getResponseJson();
        }

        @Override
        protected void onPostExecute(String responseJson) {
            super.onPostExecute(responseJson);
            TransactionApiResponse transactionApiResponse = TransactionApiResponse.fromJsonString(responseJson);
            handleApiResponse(transactionApiResponse);
        }
    }

    private class AddressVerificationAsyncTask extends AsyncTask<String, Void, Address> {


        @Override
        protected Address doInBackground(String... params) {
            Intent i = new Intent(activity, AddressVerificationActivity.class);
            i.putExtra(AddressVerificationActivity.EXTRA_COUNTRY_CODE, params[0]);
            activity.startActivity(i);
            synchronized (AddressHolder.getLock()) {
                try {
                    AddressHolder.getLock().wait();
                } catch (InterruptedException e) {
                    notifyProcessingError(new Exception("Address entry Interrupted"));
                }
            }

            return addressHolder.getAddress();
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);

            if (address != null) {
                Log.e("AVS_ADDRESS", address.toString());
                chargeWithAvs(address);

            } else {
                notifyProcessingError(new Exception("No address provided"));
            }
        }
    }
}