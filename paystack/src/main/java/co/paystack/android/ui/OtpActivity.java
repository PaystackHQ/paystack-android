package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import co.paystack.android.R;
import co.paystack.android.design.widget.PinPadView;

public class OtpActivity extends Activity {

    final OtpSingleton si = OtpSingleton.getInstance();
    private PinPadView pinpadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_otp);
        setTitle("ENTER OTP");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pinpadView = findViewById(R.id.pinpadView);
        setup();
    }

    protected void setup() {


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

    public void onDestroy() {
        super.onDestroy();
        handleSubmit("");
    }

    public void handleSubmit(String otp){
        synchronized (si) {
            si.setOtp(otp);
            si.notify();
        }
        finish();
    }
}
