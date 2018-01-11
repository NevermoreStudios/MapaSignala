package com.nevermore.mapasignala.server;

import com.nevermore.mapasignala.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public final API api;

    public APIClient() {
        this.api = new Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API.class);
    }
}
