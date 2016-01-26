package co.paystack.android.utils;

import android.util.Log;

/**
 * Logger util class
 *
 * @author {androidsupport@paystack.co} on 9/18/15.
 */
public class Logger {

  public static boolean DEBUG_ON = false;
  public static String DEFAULT_TAG = "Paystack";

  public static void d(String tag, String message) {
    if (DEBUG_ON)
      Log.d(tag, message);
  }

  public static void d(String message) {
    d(DEFAULT_TAG, message);
  }


  public static void e(String tag, String message) {
    if (DEBUG_ON)
      Log.e(tag, message);
  }

  public static void e(String message) {
    e(DEFAULT_TAG, message);
  }

}
