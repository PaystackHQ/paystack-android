package co.paystack.android.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import co.paystack.android.PaystackSdk;
import co.paystack.android.exceptions.PaystackSdkNotInitializedException;

/**
 * General utils class
 *
 * @author   {androidsupport@paystack.co} on 9/22/15.
 */
public class Utils {


    /**
     * Util class for validation
     */
    public static class Validate{
        /**
         * To validate if the sdk has been initialized
         *
         * @throws PaystackSdkNotInitializedException
         */
        public static void validateSdkInitialized() throws PaystackSdkNotInitializedException{
            if(!PaystackSdk.isSdkInitialized()){
                throw new PaystackSdkNotInitializedException("Paystack SDK has not been initialized." +
                        "The SDK has to be initialized before use");
            }
        }

        /**
         * To check for internet permission
         * @param context
         */
        public static void hasInternetPermission(Context context){
            validateNotNull(context, "context");
            PackageManager pm = context.getPackageManager();
            int hasPermission = pm.checkPermission(Manifest.permission.INTERNET, context.getPackageName());//context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
            if ( hasPermission ==
                    PackageManager.PERMISSION_DENIED) {
                throw new IllegalStateException("Paystack requires internet permission. " +
                        "Please add the intenet permission to your AndroidManifest.xml");
            }
        }

        public static String hasPublishableKey() throws PaystackSdkNotInitializedException {
            String publishableKey = PaystackSdk.getPublishableKey();
            if (publishableKey == null) {
                throw new IllegalStateException("No Publishable key found, please set the Publishable key.");
            }
            return publishableKey;
        }

        public static void validateNotNull(Object arg, String name) {
            if (arg == null) {
                throw new NullPointerException("Argument '" + name + "' cannot be null");
            }
        }
    }
}
