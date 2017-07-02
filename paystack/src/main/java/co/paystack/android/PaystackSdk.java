package co.paystack.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Charge;
import co.paystack.android.utils.Utils;

/**
 * This is the overall paystack sdk manager class.
 * Must be used to initialize the Sdk.
 *
 * @author {androidsupport@paystack.co} on 9/20/15.
 */
public final class PaystackSdk {

    /**
     * Value for the version code of this sdk
     */
    public static final int VERSION_CODE = BuildConfig.VERSION_CODE;
    /**
     * key for public key property in the AndroidManifest.xml
     */
    private static final String KEY_PUBLIC_KEY_PROP = "co.paystack.android.PublicKey";
    public static Context applicationContext;
    /**
     * Flag to know if sdk has been initialized
     */
    private static boolean sdkInitialized;
    /**
     * Reference to the public key
     */
    private static volatile String publicKey;

    /**
     * Initialize PaystackSdk with a callback when has been initilized successfully.
     *
     * @param applicationContext - Application Context
     * @param initializeCallback - callback to execute after initializing
     */
    private static synchronized void initialize(Context applicationContext, SdkInitializeCallback initializeCallback) {
        //do all the init work here

        //check if initialize callback is set and sdk is actually intialized
        if (initializeCallback != null && sdkInitialized) {
            initializeCallback.onInitialized();
            return;
        }

        //null check for applicationContext
        Utils.Validate.validateNotNull(applicationContext, "applicationContext");

        //check for internet permissions
        Utils.Validate.hasInternetPermission(applicationContext);

        //load data from PaystackSdk
        PaystackSdk.loadFromManifest(applicationContext);

        sdkInitialized = true;
        PaystackSdk.applicationContext = applicationContext;

        if (initializeCallback != null) {
            initializeCallback.onInitialized();
        }
    }

    /**
     * Initialize an sdk without a callback
     *
     * @param context - Application Context
     */
    public static synchronized void initialize(Context context) {
        initialize(context, null);
    }


    public static boolean isSdkInitialized() {
        return sdkInitialized;
    }

    /**
     * Return public key
     *
     * @return public key
     * @throws PaystackSdkNotInitializedException if the sdk hasn't been initialized
     */
    public static String getPublicKey() throws PaystackSdkNotInitializedException {
        //validate that the sdk has been initialized
        Utils.Validate.validateSdkInitialized();

        return publicKey;
    }

    /**
     * Sets the public key
     *
     * @param publicKey - App Developer's public key
     */
    public static void setPublicKey(String publicKey) {
        PaystackSdk.publicKey = publicKey;
    }

    private static void loadFromManifest(Context context) {
        if (context == null) {
            return;
        }

        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA
            );
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        //check if we can get any metadata, return if not
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return;
        }

        //check public key
        if (publicKey == null) {
            publicKey = applicationInfo.metaData.getString(KEY_PUBLIC_KEY_PROP);
        }

    }

    private static void performChecks() {
        //validate that sdk has been initialized
        Utils.Validate.validateSdkInitialized();

        //validate public keys
        Utils.Validate.hasPublicKey();
    }

    public static void chargeCard(Activity activity, Charge charge, Paystack.TransactionCallback transactionCallback) {
        if (BuildConfig.DEBUG && (activity == null)) {
            throw new AssertionError("activity must not be null");
        }

        performChecks();

        //construct paystack object
        Paystack paystack = new Paystack(PaystackSdk.getPublicKey());

        //create token
        paystack.chargeCard(activity, charge, transactionCallback);
    }

    public interface SdkInitializeCallback {
        void onInitialized();
    }

}
