package co.paystack.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.paystack.android.design.widget.PinPadView;

import co.paystack.android.R;

public class PinActivity extends AppCompatActivity {

    private PinPadView pinpadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        setTitle("ENTER CARD PIN");
    }

    @Override
    protected void onResume() {
        super.onResume();

        pinpadView = (PinPadView) findViewById(R.id.pinpadView);

        pinpadView.setOnCompletedListener(new PinPadView.OnCompletedListener() {
            @Override
            public void onCompleted(String pin) {
                PinSingleton si = PinSingleton.getInstance();
                synchronized (si) {
                    si.setPin(pin);
                    si.notify();

                }
                PinActivity.this.finish();
            }
        });

    }
}
