package co.paystack.example;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class MainActivity extends AppCompatActivity {

    // To get started quickly, change this to your heroku deployment of
    // https://github.com/PaystackHQ/sample-charge-card-backend
    // Step 1. Visit https://github.com/PaystackHQ/sample-charge-card-backend
    // Step 2. Click "Deploy to heroku"
    // Step 3. Login with your heroku credentials or create a free heroku account
    // Step 4. Provide your secret key and an email with which to start all test transactions
    // Step 5. Copy the url generated by heroku (format https://some-url.herokuapp.com) into the space below
    String backend_url = "https://infinite-peak-60063.herokuapp.com";
    // Set this to a public key that matches the secret key you supplied while creating the heroku instance
    String paystack_public_key = "pk_test_9eb0263ed776c4c892e0281348aee4136cd0dd52";

    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;

    TextView mTextError;
    TextView mTextBackendMessage;

    ProgressDialog dialog;
    private TextView mTextReference;
    private Charge charge;
    private Transaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG && (backend_url.equals(""))) {
            throw new AssertionError("Please set a backend url before running the sample");
        }
        if (BuildConfig.DEBUG && (paystack_public_key.equals(""))) {
            throw new AssertionError("Please set a public key before running the sample");
        }

        PaystackSdk.setPublicKey(paystack_public_key);

        mEditCardNum = findViewById(R.id.edit_card_number);
        mEditCVC = findViewById(R.id.edit_cvc);
        mEditExpiryMonth = findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = findViewById(R.id.edit_expiry_year);

        Button mButtonPerformTransaction = findViewById(R.id.button_perform_transaction);
        Button mButtonPerformLocalTransaction = findViewById(R.id.button_perform_local_transaction);

        mTextError = findViewById(R.id.textview_error);
        mTextBackendMessage = findViewById(R.id.textview_backend_message);
        mTextReference = findViewById(R.id.textview_reference);

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());

        //set click listener
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startAFreshCharge(false);
                } catch (Exception e) {
                    MainActivity.this.mTextError.setText(String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));

                }
            }
        });
        mButtonPerformLocalTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startAFreshCharge(true);
                } catch (Exception e) {
                    MainActivity.this.mTextError.setText(String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));

                }
            }
        });
    }

    private void startAFreshCharge(boolean local) {
        // initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Performing transaction... please wait");
        dialog.show();

        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception

            charge.setAmount(2000);
            charge.setEmail("customer@email.com");
            charge.setReference("ChargedFromAndroid_" + Calendar.getInstance().getTimeInMillis());
            try {
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chargeCard();
        } else {
            // Perform transaction/initialize on our server to get an access code
            // documentation: https://developers.paystack.co/reference#initialize-a-transaction
            new fetchAccessCodeFromServer().execute(backend_url + "/new-access-code");
        }
    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum = mEditCardNum.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = mEditCVC.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = 0;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(MainActivity.this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
                dismissDialog();

                MainActivity.this.transaction = transaction;
                mTextError.setText(" ");
                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                updateTextViews();
                new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                MainActivity.this.transaction = transaction;
                Toast.makeText(MainActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                updateTextViews();
            }

            @Override
            public void showLoading(Boolean isProcessing) {
                if (isProcessing) {
                    Toast.makeText(MainActivity.this, "Processing...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                MainActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    MainActivity.this.startAFreshCharge(false);
                    MainActivity.this.chargeCard();
                    return;
                }

                dismissDialog();

                if (transaction.getReference() != null) {
                    Toast.makeText(MainActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
                updateTextViews();
            }

        });
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void updateTextViews() {
        if (transaction.getReference() != null) {
            mTextReference.setText(String.format("Reference: %s", transaction.getReference()));
        } else {
            mTextReference.setText("No transaction");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean isEmpty(String s) {
        return s == null || s.length() < 1;
    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                charge.setAccessCode(result);
                chargeCard();
            } else {
                MainActivity.this.mTextBackendMessage.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {
            try {
                URL url = new URL(ac_url[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }

    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                MainActivity.this.mTextBackendMessage.setText(String.format("Gateway response: %s", result));

            } else {
                MainActivity.this.mTextBackendMessage.setText(String.format("There was a problem verifying %s on the backend: %s ", this.reference, error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                URL url = new URL(backend_url + "/verify/" + this.reference);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }
}
