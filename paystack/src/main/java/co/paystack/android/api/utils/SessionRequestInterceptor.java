package co.paystack.android.api.utils;

import co.paystack.android.Config;
import retrofit.RequestInterceptor;

/**
 * Created by   {androidsupport@paystack.co} on 9/18/15.
 */
public class SessionRequestInterceptor implements RequestInterceptor {

    String USER_AGENT = "Android_" + Config.VERSION_CODE;
    String CHARSET = "UTF-8";

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Charset", CHARSET);
    }
}
