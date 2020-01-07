package com.example.graduationproject;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.form.FormDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String url;
    private Context mContext;
    private static NetworkManager networkManager=null;
    private NetworkManager(Context context){
        this.mContext=context;
        url=mContext.getString(R.string.baseUrl);
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService=retrofit.create(RetrofitService.class);
    }
    public static NetworkManager getInstance(Context context){
        if(networkManager==null){
            networkManager=new NetworkManager(context);
        }
        return networkManager;
    }
    public void formUpload(String jsonObject){

    }
    public  class Request extends AsyncTask<Integer,Integer,Void>{
        String userID;
        JSONObject jsonObject;
        String title;
        String description;
        public Request(String id,JSONObject jsonObject,String title,String description){
            this.userID=id;
            this.jsonObject=jsonObject;
            this.title=title;
            this.description=description;
            Log.v("테스트","생성자호출"+id);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            submit(jsonObject);
            return null;
        }
    }

    public void submit(JSONObject jsonObject){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("json",jsonObject.toString())
                .build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"save")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.v("테스트","폼 전송 실패");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    public void toastMessage(final String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
}
