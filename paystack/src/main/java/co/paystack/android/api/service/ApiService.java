package co.paystack.android.api.service;

import java.util.HashMap;

import co.paystack.android.api.model.TokenApiResponse;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * ApiService
 */
public interface ApiService {

  /**
   * call for creating token
   *
   * @param fields
   * @param callback
   */
  @FormUrlEncoded
  @POST("/bosco/createmobiletoken")
  void createToken(@FieldMap HashMap<String, String> fields, Callback<TokenApiResponse> callback);


}
