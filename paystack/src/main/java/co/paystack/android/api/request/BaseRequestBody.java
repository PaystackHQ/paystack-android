package co.paystack.android.api.request;

import java.util.HashMap;

/**
 * A base for all request bodies
 */
public abstract class BaseRequestBody {
    public abstract HashMap<String, String> getParamsHashMap();
}
