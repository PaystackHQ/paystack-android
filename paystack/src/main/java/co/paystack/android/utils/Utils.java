package co.paystack.android.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import java.text.NumberFormat;
import java.util.Locale;

import co.paystack.android.PaystackSdk;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;

/**
 * General utils class
 *
 * @author {androidsupport@paystack.co} on 9/22/15.
 */
public class Utils {


  /**
   * Util class for validation
   */
  public static class Validate {
    /**
     * To validate if the sdk has been initialized
     *
     * @throws PaystackSdkNotInitializedException - An exception that let's you know that you
     *          did not initialize the sdk before the call was made
     */
    public static void validateSdkInitialized() throws PaystackSdkNotInitializedException {
      if (!PaystackSdk.isSdkInitialized()) {
        throw new PaystackSdkNotInitializedException("Paystack SDK has not been initialized." +
            "The SDK has to be initialized before use");
      }
    }

    /**
     * To check for internet permission
     *
     * @param context - Application context for current run
     */
    public static void hasInternetPermission(Context context) {
      validateNotNull(context, "context");
      PackageManager pm = context.getPackageManager();
      int hasPermission = pm.checkPermission(Manifest.permission.INTERNET, context.getPackageName());//context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
      if (hasPermission ==
          PackageManager.PERMISSION_DENIED) {
        throw new IllegalStateException("Paystack requires internet permission. " +
            "Please add the intenet permission to your AndroidManifest.xml");
      }
    }

      public static String hasPublicKey() throws PaystackSdkNotInitializedException {
          String publicKey = PaystackSdk.getPublicKey();
          if (publicKey == null) {
        throw new IllegalStateException("No Public key found, please set the Public key.");
      }
          return publicKey;
    }

    public static void validateNotNull(Object arg, String name) {
      if (arg == null) {
        throw new NullPointerException("Argument '" + name + "' cannot be null");
      }
    }
  }

  public static String formatCurrency(int amount){
    Locale local = new Locale("yor", "NG");
    NumberFormat n = NumberFormat.getCurrencyInstance(local);
    return n.format(amount / 100.0);
  }


    public static String encrypt(String text){
        if (text.length() > 4){
            String start = text.substring(0, 5);
            int l = text.length() -start.length();
            StringBuilder starBuilder = new StringBuilder();
            char mChar;
            if(l >-1){
                for(int i=start.length(); i < text.length(); i++){
                    if(i < text.length()-2){
                        mChar ='*';
                    }else{
                        mChar =text.charAt(i);
                    }
                    starBuilder.append(String.valueOf(mChar));
                }
            }

            return start.concat(starBuilder.toString());
        }
        return text;
    }

}
