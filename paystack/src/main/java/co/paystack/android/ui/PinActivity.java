package co.paystack.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.paystack.android.R;
import me.philio.pinentry.PinEntryView;

public class PinActivity extends Activity {

    private Button continueButton;
    private PinEntryView pinEntryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        setTitle("ENTER CARD PIN");
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueButton = (Button) findViewById(R.id.continueButton);
        pinEntryView = (PinEntryView) findViewById(R.id.pinEntryView);
        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PinSingleton si = PinSingleton.getInstance();
                synchronized (si) {
                    si.setPin(pinEntryView.getText().toString());
                    si.notify();

                }
                PinActivity.this.finish();
            }
        });
    }
}
