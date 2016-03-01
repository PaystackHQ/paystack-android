package co.paystack.android.api.service;

import java.util.HashMap;

import co.paystack.android.api.model.TokenApiResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * ApiService
 */
public interface ApiService {

  /**
   * call for creating token
   *
   * @param fields - the fields of postData to send
   * @return Call - a call that can be executed directly `call.execute()` (synchronous implementation)
   *            or enqueued `call.enqueue(params)` (asynchronous implementation)
   * @see retrofit2.Call
   */
  @FormUrlEncoded
  @POST("/bosco/createmobiletoken")
  Call<TokenApiResponse> createToken(@FieldMap HashMap<String, String> fields);

}
