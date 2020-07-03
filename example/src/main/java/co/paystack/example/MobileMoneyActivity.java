package co.paystack.example;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.paystack.android.mobilemoney.PaystackMobileMoney;
import co.paystack.android.mobilemoney.model.MobileMoneyParams;
import co.paystack.android.mobilemoney.model.MobileMoneyProvider;

public class MobileMoneyActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etPhone, etEmail, etMobileProvider;

    private MobileMoneyProvider selectedProvider;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_money);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etMobileProvider = findViewById(R.id.etMobileProvider);
        List<String> providerNames = new ArrayList<>();
        MobileMoneyProvider.getSupportedProviders()
                .forEach(mobileMoneyProvider -> providerNames.add(mobileMoneyProvider.getName()));
        ListPopupWindow providerSelectPopup = initPopupMenu(etMobileProvider, providerNames, position -> {
            selectedProvider = MobileMoneyProvider.getSupportedProviders().get(position);
            etMobileProvider.setText(selectedProvider.getName());
        });

        etMobileProvider.setOnClickListener(v -> providerSelectPopup.show());

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> {
            if (validateForm()) {
                pay();
            } else {
                Toast.makeText(this, "Invalid email, phone number or provider", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Basic form validation
     */
    public boolean validateForm() {
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        return !phone.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && selectedProvider != null;
    }

    public void pay() {
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();

        MobileMoneyParams params = new MobileMoneyParams(phone, selectedProvider);
        PaystackMobileMoney paystackMobileMoney = new PaystackMobileMoney.Builder(this, params)
                .amount(200)
                .currency("GHS")
                .email(email)
                .phoneNumber(phone)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        paystackMobileMoney.beginTransaction();
    }

    private ListPopupWindow initPopupMenu(View anchorView, List<String> items, OnPopUpItemClickListener onItemClickListener) {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this, null, R.attr.listPopupWindowStyle);
        String[] array = Arrays.copyOf(items.toArray(), items.size(), String[].class);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item, array);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            onItemClickListener.onPopUpItemClick(position);
            listPopupWindow.dismiss();
        });
        return listPopupWindow;
    }

    private interface OnPopUpItemClickListener {
        void onPopUpItemClick(int position);
    }

}
