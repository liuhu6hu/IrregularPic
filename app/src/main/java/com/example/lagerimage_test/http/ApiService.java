package com.example.lagerimage_test.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("v1/auth")
    Call<Response> registerLicense(@Field("id") String deviceID, @Field("value") String value);

    @GET("v1/info")
    Call<CommonResponse> info(@Query("device") String deviceID, @Query("license") String value);

    @GET("v1/ver/check")
    Call<VersionResponse> version();
}