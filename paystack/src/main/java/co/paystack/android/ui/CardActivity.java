package co.paystack.android.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import co.paystack.android.R;
import co.paystack.android.model.Card;

import static co.paystack.android.utils.StringUtils.isEmpty;

public class CardActivity extends AppCompatActivity {

    final CardSingleton si = CardSingleton.getInstance();
    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;
    Card card;
    private View.OnFocusChangeListener revalidate = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            validateCardForm();
        }
    };
    private boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle("ENTER CARD DETAILS");

        mEditCardNum = findViewById(R.id.edit_card_number);
        mEditCVC = findViewById(R.id.edit_cvc);
        mEditExpiryMonth = findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = findViewById(R.id.edit_expiry_year);

        synchronized (si) {
            card = si.getCard();
        }
        if (card != null) {
            mEditCardNum.setText(card.getNumber());
            mEditCVC.setText(card.getCvc());
            mEditExpiryMonth.setText(card.getExpiryMonth() == 0 ? "" : card.getExpiryMonth().toString());
            mEditExpiryYear.setText(card.getExpiryYear() == 0 ? "" : card.getExpiryYear().toString());
        }

        Button mButtonPerformTransaction = findViewById(R.id.button_perform_transaction);
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardActivity.this.validateCardForm()) {
                    CardActivity.this.handleSubmit(card);
                }
            }
        });

        mEditCardNum.setOnFocusChangeListener(revalidate);
        mEditCVC.setOnFocusChangeListener(revalidate);
        mEditExpiryMonth.setOnFocusChangeListener(revalidate);
        mEditExpiryYear.setOnFocusChangeListener(revalidate);

        mEditCardNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mEditCVC.requestFocus();
                return true;
            }
        });
        mEditCVC.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mEditExpiryMonth.requestFocus();
                return true;
            }
        });
        mEditExpiryMonth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mEditExpiryYear.requestFocus();
                return true;
            }
        });
    }

    private boolean validateCardForm() {
        //validate fields
        String cardNum = mEditCardNum.getText().toString().trim();

        if (isEmpty(cardNum)) {
            mEditCardNum.setError("Empty card number");
            return false;
        }

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        if (!card.validNumber()) {
            mEditCardNum.setError("Invalid card number");
            return false;
        }

        //validate cvc
        String cvc = mEditCVC.getText().toString().trim();
        if (isEmpty(cvc)) {
            mEditCVC.setError("Empty cvc");
            return false;
        }
        //update the cvc field of the card
        card.setCvc(cvc);

        //check that it's valid
        if (!card.validCVC()) {
            mEditCVC.setError("Invalid cvc");
            return false;
        }

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = -1;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
            return false;
        }

        if (month < 1 || month > 12) {
            mEditExpiryMonth.setError("Invalid month");
            return false;
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = -1;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
            return false;
        }

        if (year < 1) {
            mEditExpiryYear.setError("Invalid year");
            return false;
        }

        card.setExpiryYear(year);

        //validate expiry
        if (!card.validExpiryDate()) {
            mEditExpiryMonth.setError("Invalid expiry");
            mEditExpiryYear.setError("Invalid expiry");
            return false;
        }

        return true;
    }

    void handleSubmit(Card card) {
        if (submitted) {
            return;
        }
        synchronized (si) {
            si.setCard(card);
            si.notify();
        }
        finish();
        submitted = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleSubmit(null);
    }


}
