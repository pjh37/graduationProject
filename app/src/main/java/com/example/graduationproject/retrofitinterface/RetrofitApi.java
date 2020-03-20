package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

public class RetrofitApi {
    private static String baseUrl="http://pjh.cafe24app.com/";
    private static Retrofit retrofit=null;
    public static RetrofitService getService(){
        //http://192.168.35.42:8001/
        //http://pjh.cafe24app.com/
        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service=retrofit.create(RetrofitService.class);
        return service;
    }
}
