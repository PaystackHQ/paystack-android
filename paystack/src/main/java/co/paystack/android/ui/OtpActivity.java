package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;

import co.paystack.android.R;
import co.paystack.android.design.widget.PinPadView;

public class OtpActivity extends Activity {

    private PinPadView pinpadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("ENTER OTP");

        pinpadView = (PinPadView) findViewById(R.id.pinpadView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final OtpSingleton si = OtpSingleton.getInstance();

        pinpadView.setPromptText(si.getOtpMessage());

        pinpadView.setOnCompletedListener(new PinPadView.OnCompletedListener() {

            @Override
            public void onCompleted(String pin) {
                synchronized (si) {
                    si.setOtp(pin);
                    si.notify();
                }
                OtpActivity.this.finish();
            }
        });
    }
}
