package co.paystack.android.api.service;

import java.util.HashMap;

import co.paystack.android.api.model.TransactionApiResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * ApiService
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/charge/mobile_charge")
    Call<TransactionApiResponse> charge(@FieldMap HashMap<String, String> fields);

    @FormUrlEncoded
    @POST("/charge/validate")
    Call<TransactionApiResponse> validateCharge(@FieldMap HashMap<String, String> fields);

    @GET("/requery/{trans}")
    Call<TransactionApiResponse> requeryTransaction(@Path("trans") String trans);

    @FormUrlEncoded
    @POST("/charge/avs")
    Call<TransactionApiResponse> submitCardAddress(@FieldMap HashMap<String, String> fields);

}
