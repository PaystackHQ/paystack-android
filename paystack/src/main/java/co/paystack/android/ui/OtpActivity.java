package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;

import co.paystack.android.R;
import co.paystack.android.design.widget.PinPadView;

public class OtpActivity extends Activity {

    private PinPadView pinpadView;
    final OtpSingleton si = OtpSingleton.getInstance();

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


        pinpadView.setPromptText(si.getOtpMessage());
        pinpadView.setVibrateOnIncompleteSubmit(false);
        pinpadView.setAutoSubmit(false);

        pinpadView.setOnPinChangedListener(new PinPadView.OnPinChangedListener() {
            @Override
            public void onPinChanged(String oldPin, String newPin) {
                // We had set length to 10 while creating,
                // but in case some otp is longer,
                // we will keep increasing pin length
                if(newPin.length() >= pinpadView.getPinLength()){
                    pinpadView.setPinLength(pinpadView.getPinLength()+1);
                }
            }
        });

        pinpadView.setOnSubmitListener(new PinPadView.OnSubmitListener() {
            // Always submit (we never expect this to complete since
            // we keep increasing the pinLength during pinChanged event)
            // we still handle onComplete nonetheless
            @Override
            public void onCompleted(String otp) {
                handleSubmit(otp);
            }

            @Override
            public void onIncompleteSubmit(String otp) {
                handleSubmit(otp);
            }
        });
    }

    public void handleSubmit(String otp){
        synchronized (si) {
            si.setOtp(otp);
            si.notify();
        }
        finish();
    }
}
