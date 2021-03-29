package co.paystack.android.ui;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import co.paystack.android.R;
import co.paystack.android.design.widget.PinPadView;

public class PinActivity extends AppCompatActivity {

    final PinSingleton si = PinSingleton.getInstance();
    private PinPadView pinpadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_paystack_android____activity_pin);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle("ENTER CARD PIN");

        pinpadView = findViewById(R.id.pinpadView);
        setup();
    }

    protected void setup() {
        pinpadView.setOnSubmitListener(new PinPadView.OnSubmitListener() {
            @Override
            public void onCompleted(String pin) {
                PinActivity.this.handleSubmit(pin);
            }

            @Override
            public void onIncompleteSubmit(String s) {
                // warn of incomplete PIN
            }
        });

    }

    void handleSubmit(String pin) {
        synchronized (si) {
            si.setPin(pin);
            si.notify();
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleSubmit("");
    }


}
