package com.nevermore.mapasignala.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @GET("/get/{latitude}/{longitude}/all")
    Call<List<List<SignalInfo>>> signalAll(@Path("latitude") double latitude, @Path("longitude") double longitude);
    @POST("/post")
    Call<ResponseStatus> post(@Body List<SignalData> data);
}
