package com.nevermore.mapasignala.server;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @GET("/")
    Call<ServerStatus> status();
    @GET("/get/{latitude}/{longitude}")
    Call<SignalData> signal(@Path("latitude") double latitude, @Path("longitude") double longitude);
    @GET("/tile/{zoom}/{x}/{y}")
    Call<ResponseBody> tile(@Path("zoom") int zoom, @Path("x") int x, @Path("y") int y);
    @POST("/post")
    Call<ResponseStatus> post(@Body List<SignalData> data);
}
