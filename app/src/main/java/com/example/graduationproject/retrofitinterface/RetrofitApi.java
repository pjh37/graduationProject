package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
    private static Retrofit retrofit=null;
    public static RetrofitService getService(){

        retrofit=new Retrofit.Builder()
                .baseUrl("http://pjh.cafe24app.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service=retrofit.create(RetrofitService.class);
        return service;
    }
}
