package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.paystack.android.R;

public class OtpActivity extends Activity {

    private Button continueButton;
    private EditText otpEntryView;
    private TextView otpPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("ENTER OTP");
    }

    @Override
    protected void onResume() {
        super.onResume();
        final OtpSingleton si = OtpSingleton.getInstance();
        continueButton = (Button) findViewById(R.id.continueButton);
        otpEntryView = (EditText) findViewById(R.id.otpEditText);
        otpPrompt = (TextView) findViewById(R.id.otpPrompt);
        otpPrompt.setText(si.getOtpMessage());
        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                synchronized (si) {
                    si.setOtp(otpEntryView.getText().toString());
                    si.notify();
                }
                OtpActivity.this.finish();
            }
        });
    }
}
