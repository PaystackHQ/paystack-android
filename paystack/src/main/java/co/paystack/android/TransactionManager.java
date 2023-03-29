package co.paystack.android;

import static co.paystack.android.Transaction.EMPTY_TRANSACTION;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import org.jetbrains.annotations.NotNull;

import co.paystack.android.api.ApiCallback;
import co.paystack.android.api.ChargeApiCallback;
import co.paystack.android.api.PaystackRepository;
import co.paystack.android.api.model.ChargeResponse;
import co.paystack.android.api.model.TransactionInitResponse;
import co.paystack.android.api.request.ChargeParams;
import co.paystack.android.exceptions.CardException;
import co.paystack.android.exceptions.ChargeException;
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
import co.paystack.android.utils.Crypto;
import co.paystack.android.utils.StringUtils;

class TransactionManager {

    private static final String LOG_TAG = TransactionManager.class.getSimpleName();
    private static boolean PROCESSING = false;

    private Activity activity;
    private Paystack.TransactionCallback transactionCallback;

    private final CardSingleton cns = CardSingleton.getInstance();
    private final PinSingleton psi = PinSingleton.getInstance();
    private final OtpSingleton osi = OtpSingleton.getInstance();
    private final AuthSingleton asi = AuthSingleton.getInstance();
    private final AddressHolder addressHolder = AddressHolder.getInstance();

    private final PaystackRepository paystackRepository;

    ChargeApiCallback cardProcessCallback = new ChargeApiCallback() {
        @Override
        public void onSuccess(@NotNull ChargeParams params, @NotNull ChargeResponse data) {
            processChargeResponse(params, data);
            transactionCallback.showLoading(false);
        }

        @Override
        public void onError(@NotNull Throwable e, @Nullable String reference) {
            Log.e(LOG_TAG, e.getMessage(), e);
            Transaction transaction = new Transaction();
            transaction.setReference(reference);
            notifyProcessingError(e, transaction);
        }
    };

    TransactionManager(PaystackRepository paystackRepository) {
        this.paystackRepository = paystackRepository;
    }

    void chargeCard(Activity activity, String publicKey, Charge charge, Paystack.TransactionCallback transactionCallback) {
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
        this.transactionCallback = transactionCallback;

        validateCardThenInitTransaction(publicKey, charge);
    }

    private void validateCardThenInitTransaction(String publicKey, Charge charge) {
        try {
            if (charge.getCard() == null || !charge.getCard().isValid()) {
                final CardSingleton si = CardSingleton.getInstance();
                synchronized (si) {
                    si.setCard(charge.getCard());
                }
                new CardAsyncTask(publicKey, charge).execute();
            } else {
                if (TransactionManager.PROCESSING) {
                    throw new ProcessingException();
                }

                setProcessingOn();

                String deviceId = "androidsdk_" + Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                initiateTransaction(publicKey, charge, deviceId);
            }
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            if (!(ce instanceof ProcessingException)) {
                setProcessingOff();
            }
            transactionCallback.onError(ce, EMPTY_TRANSACTION);
        }
    }

    @VisibleForTesting
    void initiateTransaction(String publicKey, Charge charge, String deviceId) {
        ApiCallback<TransactionInitResponse> callback = new ApiCallback<TransactionInitResponse>() {
            @Override
            public void onSuccess(TransactionInitResponse data) {
                Card card = charge.getCard();
                String transactionId = data.getTransactionId();
                ChargeParams params = new ChargeParams(
                        Crypto.encrypt(StringUtils.concatenateCardFields(card)),
                        transactionId,
                        card.getLast4digits(),
                        deviceId,
                        data.getReference(),
                        null
                );
                processCharge(params);
            }

            @Override
            public void onError(@NotNull Throwable exception) {
                Log.e(LOG_TAG, exception.getMessage(), exception);
                notifyProcessingError(exception);
            }
        };

        transactionCallback.showLoading(true);
        if (charge.getAccessCode() == null || charge.getAccessCode().isEmpty()) {
            paystackRepository.initializeTransaction(publicKey, charge, deviceId, callback);
        } else {
            paystackRepository.getTransactionWithAccessCode(charge.getAccessCode(), callback);
        }
    }

    private void processChargeResponse(ChargeParams chargeParams, ChargeResponse chargeResponse) {
        if (chargeResponse == null) {
            notifyProcessingError(new ChargeException("Unknown server response"), chargeParams.getTransaction());
            return;
        }

        String status = chargeResponse.getStatus();
        if (status != null) {
            if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("success")) {
                setProcessingOff();
                Transaction transaction = new Transaction();
                transaction.setId(chargeResponse.getTransactionId());
                transaction.setReference(chargeResponse.getReference());
                transactionCallback.onSuccess(transaction);
                return;
            }

            if (status.equalsIgnoreCase("pending")) {
                reQueryChargeOnServer(chargeParams);
                return;
            }

            if (status.equalsIgnoreCase("requery")) {
                reQueryChargeOnServer(chargeParams);
                return;
            }
        }

        if (chargeResponse.getAuth() != null && !chargeResponse.getAuth().equalsIgnoreCase("none")) {
            authenticateTransaction(chargeParams, chargeResponse, chargeParams.getTransaction());
            return;
        }

        setProcessingOff();
        notifyProcessingError(new ChargeException(chargeResponse.getMessage()), chargeParams.getTransaction());
    }

    private void authenticateTransaction(ChargeParams chargeParams, ChargeResponse chargeResponse, Transaction transaction) {
        String authType = chargeResponse.getAuth();
        assert authType != null;

        String authMessage = "Authentication required";
        if (chargeResponse.getOtpMessage() != null) {
            authMessage = chargeResponse.getOtpMessage();
        } else if (chargeResponse.getMessage() != null) {
            authMessage = chargeResponse.getMessage();
        }

        if (authType.equalsIgnoreCase(AuthType.ADDRESS_VERIFICATION)) {
            new AddressVerificationAsyncTask(chargeParams).execute(chargeResponse.getCountryCode());
        } else if (authType.equalsIgnoreCase(AuthType.PIN)) {
            new PinAsyncTask(chargeParams).execute();
        } else if (authType.equalsIgnoreCase(AuthType.OTP) || authType.equalsIgnoreCase(AuthType.PHONE)) {
            transactionCallback.beforeValidate(transaction);
            osi.setOtpMessage(authMessage);
            new OtpAsyncTask(chargeParams).execute();
        } else if (authType.equalsIgnoreCase(AuthType.THREE_DS)) {
            transactionCallback.beforeValidate(transaction);
            asi.setTransactionId(chargeParams.getTransactionId());
            asi.setUrl(authMessage);
            new AuthAsyncTask(chargeParams).execute();
        } else {
            setProcessingOff();
            notifyProcessingError(new RuntimeException("Unknown authentication method required. Please contact Paystack"), transaction);
        }
    }

    private void validateOtp(String token, ChargeParams chargeParams) {
        try {
            transactionCallback.showLoading(true);
            paystackRepository.validateTransaction(chargeParams, token, cardProcessCallback);
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            notifyProcessingError(ce, chargeParams.getTransaction());
        }
    }

    private void chargeWithAvs(Address address, ChargeParams chargeParams) {
        try {
            transactionCallback.showLoading(true);
            paystackRepository.validateAddress(chargeParams, address, cardProcessCallback);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            notifyProcessingError(e, chargeParams.getTransaction());
        }
    }

    private void reQueryChargeOnServer(ChargeParams chargeParams) {
        try {
            new CountDownTimer(5000, 5000) {
                public void onFinish() {
                    requeryTransaction(chargeParams);
                }

                public void onTick(long millisUntilFinished) {
                }
            }.start();
        } catch (Exception ce) {
            Log.e(LOG_TAG, ce.getMessage(), ce);
            notifyProcessingError(ce, chargeParams.getTransaction());
        }
    }

    private void requeryTransaction(ChargeParams chargeParams) {
        transactionCallback.showLoading(true);
        paystackRepository.requeryTransaction(chargeParams, cardProcessCallback);
    }

    private void processCharge(ChargeParams params) {
        transactionCallback.showLoading(true);
        paystackRepository.processCardCharge(params, cardProcessCallback);
    }

    private void notifyProcessingError(Throwable t) {
        setProcessingOff();
        transactionCallback.showLoading(false);
        transactionCallback.onError(t, EMPTY_TRANSACTION);
    }

    private void notifyProcessingError(Throwable t, Transaction transaction) {
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


    @VisibleForTesting
    void setTransactionCallback(Paystack.TransactionCallback transactionCallback) {
        this.transactionCallback = transactionCallback;
    }

    private class CardAsyncTask extends AsyncTask<Void, Void, Card> {

        private final String publicKey;
        private final Charge charge;

        CardAsyncTask(String publicKey, Charge charge) {
            this.publicKey = publicKey;
            this.charge = charge;
        }

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
                validateCardThenInitTransaction(publicKey, charge);
            }
        }
    }

    private class PinAsyncTask extends AsyncTask<Void, Void, String> {
        private final ChargeParams chargeParams;

        PinAsyncTask(ChargeParams chargeParams) {
            this.chargeParams = chargeParams;
        }

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
                ChargeParams params = chargeParams.addPin(Crypto.encrypt(pin));
                processCharge(params);
            } else {
                notifyProcessingError(new Exception("PIN must be exactly 4 digits"));
            }
        }
    }

    private class OtpAsyncTask extends AsyncTask<Void, Void, String> {
        private final ChargeParams chargeParams;

        public OtpAsyncTask(ChargeParams chargeParams) {
            this.chargeParams = chargeParams;
        }

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
                validateOtp(otp, chargeParams);
            } else {
                notifyProcessingError(new Exception("You did not provide an OTP"));
            }
        }
    }


    private class AuthAsyncTask extends AsyncTask<Void, Void, String> {
        private final ChargeParams chargeParams;

        AuthAsyncTask(ChargeParams chargeParams) {
            this.chargeParams = chargeParams;
        }

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
            ChargeResponse chargeResponse = ChargeResponse.Companion.fromJsonString(responseJson);
            processChargeResponse(chargeParams, chargeResponse);
        }
    }

    private class AddressVerificationAsyncTask extends AsyncTask<String, Void, Address> {
        private final ChargeParams chargeParams;

        public AddressVerificationAsyncTask(ChargeParams chargeParams) {
            this.chargeParams = chargeParams;
        }

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
                chargeWithAvs(address, chargeParams);
            } else {

                notifyProcessingError(new Exception("No address provided"), chargeParams.getTransaction());
            }
        }
    }
}