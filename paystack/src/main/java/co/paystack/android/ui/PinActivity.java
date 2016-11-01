package co.paystack.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import co.paystack.android.R;

public class PinActivity extends AppCompatActivity {

    private Button continueButton;
    private PinEntryEditText pinEntryView;

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

        pinEntryView = (PinEntryEditText) findViewById(R.id.pinEntryView);

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
