package co.paystack.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import co.paystack.android.exceptions.PaystackSdkNotInitializedException;
import co.paystack.android.model.Card;
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
  public static final int VERSION_CODE = Config.VERSION_CODE;
  /**
   * key for publishable key property in the AndroidManifest.xml
   */
  private static final String KEY_PUBLISHABLE_KEY_PROP = "co.paystack.android.PublishableKey";
  /**
   * Flag to know if sdk has been initialized
   */
  private static boolean sdkInitialized;
  /**
   * Reference to the publishable key
   */
  private static volatile String publishableKey;
  private static volatile String merchantId;
  private static volatile String secretKey;
  private static Context applicationContext;

  /**
   * Initialize PaystackSdk with a callback when has been initilized successfully.
   *
   * @param applicationContext - Application Context
   * @param initializeCallback - callback to execute after initializing
   */
  public static synchronized void initialize(Context applicationContext, SdkInitializeCallback initializeCallback) {
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

    PaystackSdk.applicationContext = applicationContext;

    sdkInitialized = true;

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
   * Return publishable key
   *
   * @return publishable key
   * @throws PaystackSdkNotInitializedException if the sdk hasn't been initialized
   */
  public static String getPublishableKey() throws PaystackSdkNotInitializedException {
    //validate that the sdk has been initialized
    Utils.Validate.validateSdkInitialized();

    return publishableKey;
  }

  /**
   * Sets the publishable key
   *
   * @param publishableKey - App Developer's publishable key
   */
  public static void setPublishableKey(String publishableKey) {
    PaystackSdk.publishableKey = publishableKey;
  }

  public static Context getApplicationContext() {
    return applicationContext;
  }

  private static void loadFromManifest(Context context) {
    if (context == null) {
      return;
    }

    ApplicationInfo applicationInfo;
    try {
      applicationInfo = context.getPackageManager().getApplicationInfo(
          context.getPackageName(),
          PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA
      );
    } catch (PackageManager.NameNotFoundException e) {
      return;
    }

    //check if we can get any metadata, return if not
    if (applicationInfo == null || applicationInfo.metaData == null) {
      return;
    }

    //check publishable key
    if (publishableKey == null) {
      publishableKey = applicationInfo.metaData.getString(KEY_PUBLISHABLE_KEY_PROP);
    }

  }

  /**
   * Method for creating a token, using {@link PaystackSdk}. You won't need to create an instance
   * of {@link Paystack} by yourself.
   * <p/>
   * Equivalent to these two lines:
   * <p/>
   * {@code
   * Paystack paystack = new Paystack(PUBLISHABLE_KEY);
   * paystack.createToken();
   * }
   *
   * @param card     - card whose token we need to create
   * @param callback - callback to execute after creating token
   */
  public static void createToken(Card card, Paystack.TokenCallback callback) {
    //validate that sdk has been initialized
    Utils.Validate.validateSdkInitialized();

    //validate publishable keys
    Utils.Validate.hasPublishableKey();

    //construct paystack object
    Paystack paystack = new Paystack(PaystackSdk.getPublishableKey());

    //create token
    paystack.createToken(card, callback);
  }

  public interface SdkInitializeCallback {
    void onInitialized();
  }

}
