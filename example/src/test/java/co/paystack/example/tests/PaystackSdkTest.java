package co.paystack.example.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.example.App;
import co.paystack.example.BuildConfig;

import static org.junit.Assert.assertTrue;

/**
 * Created by Segun Famisa {segunfamisa@gmail.com} on 9/22/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class /*, sdk = 21, manifest = "src/main/AndroidManifest.xml"*/)
public class PaystackSdkTest {

    private static final String TEST_PUBLISHABLE_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANIsL+RHqfkBiKGn/D1y1QnNrMkKzxWP\n" +
            "2wkeSokw2OJrCI+d6YGJPrHHx+nmb/Qn885/R01Gw6d7M824qofmCvkCAwEAAQ==";


//    @Test(expected = PaystackSdkNotInitializedException.class)
//    public void initPaystackWithoutInitPaystackSdkShouldThrowException(){
//        Paystack paystack = new Paystack();
//    }

//    @Test(expected = Exception.class)
//    public void initPaystackSdkWithoutPaystackActivityShouldThrowException(){
//        PaystackSdk.initialize(App.getAppContext());
//    }

    @Test(expected = NullPointerException.class)
    public void initPaystackSdkWithNullParamsShouldThrowException(){
        PaystackSdk.initialize(null);
    }

    @Test
    public void initPaystackSdkWithPaystackActivityShouldPass(){
        PaystackSdk.initialize(App.getAppContext());
        assertTrue(PaystackSdk.isSdkInitialized());
    }



}
