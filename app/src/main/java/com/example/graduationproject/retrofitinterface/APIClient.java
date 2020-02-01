package com.example.graduationproject.retrofitinterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit=null;
    public static Retrofit getClient(){
        OkHttpClient client=new OkHttpClient.Builder().build();
        retrofit=new Retrofit.Builder()
                .baseUrl("http://pjh.cafe24app.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
