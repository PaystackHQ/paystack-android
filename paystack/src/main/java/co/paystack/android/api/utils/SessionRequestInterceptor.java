package co.paystack.android.api.utils;

import android.util.Log;

import java.io.IOException;

import co.paystack.android.Config;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Session Request interceptor
 *
 * Add User agent to the headers sent by our request, not yet used
 */
public class SessionRequestInterceptor implements Interceptor {
  public static final String LOG_TAG = SessionRequestInterceptor.class.getSimpleName();
  String USER_AGENT = "Android_" + Config.VERSION_CODE;
  String CHARSET = "UTF-8";
  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request originalRequest = chain.request();
    Request moddedRequest = originalRequest;
    // Only add User-Agent header if not already there
    if (originalRequest.header("User-Agent") == null) {
      moddedRequest = moddedRequest.newBuilder()
          .header("User-Agent", USER_AGENT)
          .build();
      Log.d(LOG_TAG, "Added User-Agent Header");
    }

    // Only add Charset header if not already there
    if (originalRequest.header("User-Agent") == null) {
      moddedRequest = moddedRequest.newBuilder()
          .header("Charset", CHARSET)
          .build();
      Log.d(LOG_TAG, "Added Charset Header");
    }

    return chain.proceed(moddedRequest);
  }
}
