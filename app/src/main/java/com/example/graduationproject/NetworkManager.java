package com.example.graduationproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.retrofitinterface.RetrofitService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private String url;
    private Context mContext;
    private OkHttpClient client;
    private static NetworkManager networkManager=null;
    private NetworkManager(Context context){
        this.mContext=context;
        client=new OkHttpClient();
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
        Log.v("테스트","서버로 폼 전송 : "+jsonObject.toString());

        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("json",jsonObject.toString());
        try{
            JSONArray jsonArray=jsonObject.getJSONArray("formComponents");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                if(jsonObject1.getInt("type")==12){
                    File file=(File)jsonObject1.get("real_file_data");
                    builder.addFormDataPart(String.valueOf(jsonObject1.getInt("real_file_name")),file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")));
                }
            }
        }catch (Exception e){e.printStackTrace();}

        RequestBody requestbody=builder.build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"save")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.v("테스트","폼 전송 실패 : "+e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
    public void update(JSONObject jsonObject,int form_id){
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("form_id",String.valueOf(form_id))
                .addFormDataPart("json",jsonObject.toString())
                .build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.baseUrl)+"update")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
