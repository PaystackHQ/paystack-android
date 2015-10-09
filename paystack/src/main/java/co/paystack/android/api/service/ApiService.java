package co.paystack.android.api.service;

import java.util.HashMap;

import co.paystack.android.api.model.TokenApiResponse;
import co.paystack.android.api.request.TokenRequestBody;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * @author   {androidsupport@paystack.co} on 9/17/15.
 */
public interface ApiService {

    /**
     * ApiService for creating token
     * @param fields
     * @param callback
     */
    @FormUrlEncoded
    @POST("/bosco/createmobiletoken")
    public void createToken(@FieldMap HashMap<String, String> fields, Callback<TokenApiResponse> callback);




}
