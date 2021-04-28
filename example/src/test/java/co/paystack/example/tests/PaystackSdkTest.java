package co.paystack.example.tests;

import org.junit.Test;

import co.paystack.android.PaystackSdk;
import co.paystack.example.App;

import static org.junit.Assert.assertTrue;

/**
 * PaystackSdk Test Class
 * <p/>
 * Tests the paystack sdk
 */

public class PaystackSdkTest {

    private static final String TEST_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANIsL+RHqfkBiKGn/D1y1QnNrMkKzxWP\n" +
            "2wkeSokw2OJrCI+d6YGJPrHHx+nmb/Qn885/R01Gw6d7M824qofmCvkCAwEAAQ==";

    @Test(expected = NullPointerException.class)
    public void initPaystackSdkWithNullParamsShouldThrowException() {
        PaystackSdk.initialize(null);
    }

    //TODO: Look at this SDK Unit Test initPaystackSdkWithPaystackActivityShouldPass
    
}
