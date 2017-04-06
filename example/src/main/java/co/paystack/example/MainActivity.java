package co.paystack.example;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class MainActivity extends AppCompatActivity {

    // To get started quickly, change this to your heroku deployment of
    // https://github.com/PaystackHQ/sample-charge-card-backend
    String new_access_code_url = "https://infinite-peak-60063.herokuapp.com/new-access-code/";
    String verify_url = "https://infinite-peak-60063.herokuapp.com/verify-with-paystack/";
    // Set this to a public key that matches the secret key you supplied while creating the heroku instance
    String paystack_public_key = "pk_live_2bf31d4aea08ab31f5d0cfd645c7e4f67025d259";

    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;

    TextView mTextError;

    Card card;

    ProgressDialog dialog;
    private Button mButtonPerformTransaction;
    private TextView mTextReference;
    private Charge charge;
    private Transaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG && (new_access_code_url.equals(""))) {
            throw new AssertionError("Please set a url at which to get an access code before running the sample");
        }
        if (BuildConfig.DEBUG && (verify_url.equals(""))) {
            throw new AssertionError("Please set a url at which to verify transactions before running the sample");
        }
        if (BuildConfig.DEBUG && (paystack_public_key.equals(""))) {
            throw new AssertionError("Please set a public key before running the sample");
        }

        PaystackSdk.setPublicKey(paystack_public_key);

        mEditCardNum = (EditText) findViewById(R.id.edit_card_number);
        mEditCVC = (EditText) findViewById(R.id.edit_cvc);
        mEditExpiryMonth = (EditText) findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = (EditText) findViewById(R.id.edit_expiry_year);

        mButtonPerformTransaction = (Button) findViewById(R.id.button_perform_transaction);

        mTextError = (TextView) findViewById(R.id.textview_error);
        mTextReference = (TextView) findViewById(R.id.textview_reference);

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());

        //set click listener
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate form
                validateCardForm();
                //check card validity
                if (card != null && card.isValid()) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Performing transaction... please wait");
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    dialog.show();

                    try {
                        startAFreshCharge();
                    } catch (Exception e) {
                        MainActivity.this.mTextError.setText(String.format("An error occured hwile charging card: %s", e.getMessage()));
                }

            }
            }
        });
    }

    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        // Perform transaction/initialize on our server to get an access code
        // documentation: https://developers.paystack.co/reference#initialize-a-transaction

        new fetchAccessCodeFromServer().execute(new_access_code_url);

    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private void validateCardForm() {
        //validate fields
        String cardNum = mEditCardNum.getText().toString().trim();

        if (isEmpty(cardNum)) {
            mEditCardNum.setError("Empty card number");
            return;
        }

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        if (!card.validNumber()) {
            mEditCardNum.setError("Invalid card number");
            return;
        }

        //validate cvc
        String cvc = mEditCVC.getText().toString().trim();
        if (isEmpty(cvc)) {
            mEditCVC.setError("Empty cvc");
            return;
        }
        //update the cvc field of the card
        card.setCvc(cvc);

        //check that it's valid
        if (!card.validCVC()) {
            mEditCVC.setError("Invalid cvc");
            return;
        }

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = -1;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        if (month < 1) {
            mEditExpiryMonth.setError("Invalid month");
            return;
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = -1;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }

        if (year < 1) {
            mEditExpiryYear.setError("invalid year");
            return;
        }

        card.setExpiryYear(year);

        //validate expiry
        if (!card.validExpiryDate()) {
            mEditExpiryMonth.setError("Invalid expiry");
            mEditExpiryYear.setError("Invalid expiry");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing()){
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
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                MainActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    MainActivity.this.startAFreshCharge();
                    MainActivity.this.chargeCard();
                    return;
                }

                dismissDialog();

                if (transaction.getReference() != null) {
                    Toast.makeText(MainActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("%s  concluded with error: %s", transaction.getReference(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("Error: %s", error.getMessage()));
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
                charge.setCard(card);
                chargeCard();
            } else {
                MainActivity.this.mTextError.setText(String.format("There was a problem getting a new access code form the server: %s", error));
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
                error = e.getMessage();
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
                MainActivity.this.mTextError.setText(String.format("Gateway response: %s", result));

            } else {
                MainActivity.this.mTextError.setText(String.format("There was a problem verifying %s on the backend: %s ", this.reference, error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                URL url = new URL(verify_url + this.reference);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getMessage();
            }
            return null;
        }
    }
}
