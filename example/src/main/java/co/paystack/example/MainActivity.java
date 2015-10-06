package co.paystack.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Card;
import co.paystack.android.model.Token;

public class MainActivity extends AppCompatActivity {

    //private static final String PUBLISHABLE_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANIsL+RHqfkBiKGn/D1y1QnNrMkKzxWP2wkeSokw2OJrCI+d6YGJPrHHx+nmb/Qn885/R01Gw6d7M824qofmCvkCAwEAAQ==";


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditCardNum = (EditText)findViewById(R.id.edit_card_number);
        mEditCVC = (EditText)findViewById(R.id.edit_cvc);
        mEditExpiryMonth = (EditText)findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = (EditText)findViewById(R.id.edit_expiry_year);

        mButtonCreateToken = (Button)findViewById(R.id.button_create_token);

        mTextCard = (TextView)findViewById(R.id.textview_card);
        mTextToken = (TextView)findViewById(R.id.textview_token);

        dialog = new ProgressDialog(this);

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());


        //set click listener
        mButtonCreateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate form
                validateForm();

                //check card validity
                if(card.isValid()){
                    dialog.setMessage("Request token please wait");
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);

                    dialog.show();

                    createToken(card);
                }
            }
        });
    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private void validateForm(){
        //validate fields
        String cardNum = mEditCardNum.getText().toString().trim();

        if(isEmpty(cardNum)){
            mEditCardNum.setError("Empty card number");
            return;
        }

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        if(!card.validNumber()){
            mEditCardNum.setError("Invalid card number");
            return;
        }

        //validate cvc
        String cvc = mEditCVC.getText().toString().trim();
        if(isEmpty(cvc)){
            mEditCVC.setError("Empty cvc");
            return;
        }
        //update the cvc field of the card
        card.setCvc(cvc);

        //check that it's valid
        if(!card.validCVC()){
            mEditCVC.setError("Invalid cvc");
            return;
        }

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = -1;
        try{
            month = Integer.parseInt(sMonth);
        }
        catch (Exception e){}

        if(month < 1){
            mEditExpiryMonth.setError("Invalid month");
            return;
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = -1;
        try{
            year = Integer.parseInt(sYear);
        }
        catch(Exception e){}

        if(year < 1){
            mEditExpiryYear.setError("invalid year");
            return;
        }

        card.setExpiryYear(year);

        //validate expiry
        if(!card.validExpiryDate()){
            mEditExpiryMonth.setError("Invalid expiry");
            mEditExpiryYear.setError("Invalid expiry");
            return;
        }
    }

    private void createToken(Card card){
        //then create token using PaystackSdk class
        PaystackSdk.createToken(card, new Paystack.TokenCallback() {
            @Override
            public void onCreate(Token token) {

                //here you retrieve the token, and send to your server for charging.
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

                Toast.makeText(MainActivity.this, token.token, Toast.LENGTH_LONG).show();
                MainActivity.this.token = token;
                updateTextViews(token);
            }

            @Override
            public void onError(Exception error) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                updateTextViews(null);
            }
        });
    }

    private void updateTextViews(Token token){
        if(token != null){
            mTextCard.setText("Card last 4 digits: " + token.last4);
            mTextToken.setText("Token: " + token.token);
        }
        else{
            mTextCard.setText("Unable to get token");
            mTextToken.setText("Token was null");
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


    private boolean isEmpty(String s){
        return s == null || s.length() < 1;
    }
}
