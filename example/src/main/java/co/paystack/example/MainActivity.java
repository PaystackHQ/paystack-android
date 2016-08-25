package co.paystack.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import co.paystack.android.model.Token;
import co.paystack.android.model.Transaction;

public class MainActivity extends AppCompatActivity {

    //private static final String PUBLIC_KEY = "your public key";


    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;
    Button mButtonCreateToken;

    TextView mTextCard;
    TextView mTextToken;

    Token token;
    Card card;

    ProgressDialog dialog;
    private EditText mEditAmountInKobo;
    private EditText mEditEmail;
    private Button mButtonPerformTransaction;
    private TextView mTextReference;
    private Charge charge;
    private Transaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditCardNum = (EditText) findViewById(R.id.edit_card_number);
        mEditCVC = (EditText) findViewById(R.id.edit_cvc);
        mEditExpiryMonth = (EditText) findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = (EditText) findViewById(R.id.edit_expiry_year);
        mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditAmountInKobo = (EditText) findViewById(R.id.edit_amount_in_kobo);

        mButtonCreateToken = (Button) findViewById(R.id.button_create_token);
        mButtonPerformTransaction = (Button) findViewById(R.id.button_perform_transaction);

        mTextCard = (TextView) findViewById(R.id.textview_card);
        mTextToken = (TextView) findViewById(R.id.textview_token);
        mTextReference = (TextView) findViewById(R.id.textview_reference);

        dialog = new ProgressDialog(this);

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());


        //set click listener
        mButtonCreateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate form
                validateCardForm();

                //check card validity
                if (card.isValid()) {
                    dialog.setMessage("Request token please wait");
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    dialog.show();

                    createToken(card);
                }
            }
        });

        //set click listener
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate form
                validateTransactionForm();

                //check card validity
                if (card.isValid()) {
                    dialog.setMessage("Performing transaction... please wait");
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    dialog.show();

                    performTransaction();
                }
            }
        });
    }

    private void validateTransactionForm() {

        validateCardForm();

        charge = new Charge();
        charge.setCard(card);

        //validate fields
        String email = mEditEmail.getText().toString().trim();

        if (isEmpty(email)) {
            mEditEmail.setError("Empty email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditEmail.setError("Invalid email");
            return;
        }

        charge.setEmail(email);

        String sAmount = mEditAmountInKobo.getText().toString().trim();
        int amount = -1;
        try {
            amount = Integer.parseInt(sAmount);
        } catch (Exception ignored) {
        }

        if (amount < 1) {
            mEditExpiryMonth.setError("Invalid amount");
            return;
        }

        charge.setAmount(amount);


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

    private void performTransaction() {
        transaction = null;
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onValidate(Transaction transaction) {
                // This is called only after transaction is successful
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                MainActivity.this.transaction = transaction;
                Toast.makeText(MainActivity.this, transaction.reference, Toast.LENGTH_LONG).show();
                updateTextViews();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP
                // Save reference so you may send to server if
                // error occurs with OTP
                MainActivity.this.transaction = transaction;
                Toast.makeText(MainActivity.this, transaction.reference, Toast.LENGTH_LONG).show();
                updateTextViews();
            }

            @Override
            public void onError(Throwable error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (MainActivity.this.transaction == null) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextReference.setText(String.format("Error: %s", error.getMessage()));
                } else {
                    Toast.makeText(MainActivity.this, transaction.reference + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextCard.setText(String.format("%s  concluded with error: %s", transaction.reference, error.getMessage()));
                }
                updateTextViews();
            }

        });
    }

    private void createToken(Card card) {
        //then create token using PaystackSdk class
        PaystackSdk.createToken(card, new Paystack.TokenCallback() {
            @Override
            public void onCreate(Token token) {

                //here you retrieve the token, and send to your server for charging.
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Toast.makeText(MainActivity.this, token.token, Toast.LENGTH_LONG).show();
                MainActivity.this.token = token;
                updateTextViews();
            }

            @Override
            public void onError(Throwable error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                updateTextViews();
            }
        });
    }

    private void updateTextViews() {
        if (token != null) {
            mTextCard.setText(String.format("Card last 4 digits: %s", token.last4));
            mTextToken.setText(String.format("Token: %s", token.token));
        } else if (transaction != null) {
            mTextReference.setText(String.format("Reference: %s", transaction.reference));
        } else {
            mTextCard.setText(R.string.token_not_gotten);
            mTextToken.setText(R.string.token_null_message);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
