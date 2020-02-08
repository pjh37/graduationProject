package com.example.graduationproject.retrofitinterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
    private static Retrofit retrofit=null;
    public static RetrofitService getService(){

        retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.35.42:8001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service=retrofit.create(RetrofitService.class);
        return service;
    }
}
