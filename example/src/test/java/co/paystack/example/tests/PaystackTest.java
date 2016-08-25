//package co.paystack.example.tests;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.annotation.Config;
//
//import co.paystack.android.Paystack;
//import co.paystack.android.exceptions.AuthenticationException;
//import co.paystack.android.model.Card;
//import co.paystack.android.model.Token;
//import co.paystack.android.utils.Logger;
//import co.paystack.example.BuildConfig;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by Segun Famisa {segunfamisa@gmail.com} on 9/17/15.
// */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class /*, sdk = 21, manifest = "src/main/AndroidManifest.xml"*/)
//public class PaystackTest {
//
//    private static final String DUMMY_PUBLIC_KEY = "";
//    private static final Card NULL_CARD = new Card(null, null, null, null);
//    private static final Card VALID_CARD = new Card.Builder("4123450131001381", 2017, 03, "883").build();
//    private static final Paystack.TokenCallback DUMMY_TOKEN_CALLBACK = new Paystack.TokenCallback() {
//        @Override
//        public void onCreate(Token token) {
//            if(token != null){
//                System.out.println(token.token);
//            }
//        }
//
//        @Override
//        public void onError(Exception error) {
//            if(error != null){
//                System.out.println(error.toString());
//            }
//        }
//    };
//
//
////    @Test(expected = RuntimeException.class)
////    public void createTokenShouldFailWithoutCard(){
////        Paystack paystack = new Paystack();
////        paystack.createToken(null, DUMMY_PUBLIC_KEY, DUMMY_TOKEN_CALLBACK);
////    }
////
////    //@Test(expected = AuthenticationException.class)
////    public void createTokenShouldFailWithNullPublicKey(){
////        Paystack paystack = new Paystack();
////        paystack.createToken(VALID_CARD, null, DUMMY_TOKEN_CALLBACK);
////    }
////
////    @Test(expected = RuntimeException.class)
////    public void createTokenShouldFailWithNullTokenCallback(){
////        Paystack paystack = new Paystack();
////        paystack.createToken(VALID_CARD, DUMMY_PUBLIC_KEY, null);
////    }
////
////    @Test
////    public void canCreateToken(){
////        Paystack paystack = new Paystack();
////        paystack.createToken(VALID_CARD, DUMMY_PUBLIC_KEY, DUMMY_TOKEN_CALLBACK);
////        assertTrue(true);
////    }
//}
