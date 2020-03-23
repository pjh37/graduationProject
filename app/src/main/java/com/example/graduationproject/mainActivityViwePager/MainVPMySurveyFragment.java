package com.example.graduationproject.mainActivityViwePager;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.UploadedSurveyRV;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.form.FormSaveManager;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.offlineform.FormItem;
import com.example.graduationproject.offlineform.OfflineFormRVAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainVPMySurveyFragment extends Fragment {
    private static final int SERVER_SURVEY=0;
    private static final int OFFLINE_SURVEY=1;
    RecyclerView offlineSurveyRecycleView;
    RecyclerView responseWaitSurveyRecycleView;
    RecyclerView.Adapter  offlineFormAdapter;
    RecyclerView.Adapter  uploadedSurveyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtMoreOfflineView;
    private TextView txtMoreMySurveyView;
    private ArrayList<FormItem> formItem;
    private FormSaveManager formSaveManager;
    public String userEmail;
    private String url;
    private ProgressBar progressBar;
    private boolean isFinish;
    private ArrayList<UploadedSurveyDTO> datas;

    public MainVPMySurveyFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.userEmail=getArguments().getString("userEmail");
        }
        formSaveManager=FormSaveManager.getInstance(getContext());
        formItem=new ArrayList<>();
        isFinish=false;
        url=getString(R.string.baseUrl);
        datas=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_main_vp_mysurvey,container,false);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progress);
        txtMoreOfflineView=(TextView)rootView.findViewById(R.id.moreOfflineView);
        txtMoreMySurveyView=(TextView)rootView.findViewById(R.id.moreMySurveyView);
        txtMoreOfflineView.setOnClickListener(new ClickListener());
        txtMoreMySurveyView.setOnClickListener(new ClickListener());
        layoutManager=new LinearLayoutManager(getActivity());
        offlineSurveyRecycleView=(RecyclerView)rootView.findViewById(R.id.offlineSurveyRecycleView);
        responseWaitSurveyRecycleView=(RecyclerView)rootView.findViewById(R.id.responseWaitSurveyRecycleView);
        responseWaitSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        responseWaitSurveyRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),1));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.v("유저이메일들어오나",Session.getUserEmail());
        getResponseWaitSurvey(Session.getUserEmail());
        new LoadTask().execute();
    }

    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.moreOfflineView:{
                    Intent intent =new Intent(getContext(),moreViewActivity.class);
                    intent.putExtra("type",OFFLINE_SURVEY);
                    intent.putExtra("userEmail",userEmail);
                    startActivity(intent);
                    break;
                }
                case R.id.moreMySurveyView:{
                    Intent intent =new Intent(getContext(),moreViewActivity.class);
                    intent.putExtra("type",SERVER_SURVEY);
                    intent.putExtra("userEmail",userEmail);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    public class LoadTask extends AsyncTask<Void,FormItem,Void> {
        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //offlineFormAdapter.notifyDataSetChanged();
            offlineFormAdapter=new OfflineFormRVAdapter(getContext(),formItem,userEmail);
            offlineSurveyRecycleView.setAdapter(offlineFormAdapter);
            offlineSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(FormItem... values) {
            formItem.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String[] columns=new String[]{"_id","json","time"};
            Cursor cursor= formSaveManager.query(columns,null,null,null,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    FormItem item=new FormItem();
                    item.set_id(cursor.getInt(0));
                    try{
                        JSONObject jsonObject=new JSONObject(cursor.getString(1));
                        item.setTitle(jsonObject.getString("title"));
                        Log.v("테스트",jsonObject.getString("title"));
                    }catch (Exception e){e.printStackTrace();}
                    item.setTime(getTime(cursor.getString(2)));
                    publishProgress(item);
                }
                cursor.close();
            }
            return null;
        }
    }
    public void getResponseWaitSurvey(String userEmail){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
                if(isFinish){
                    getActivity().runOnUiThread(()->{ progressBar.setVisibility(View.GONE); });
                }

            }
        }).start();
        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("userEmail", Session.getUserEmail())//userEmail 부분 교체
                .build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"user/forms")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //toastMessage("폼 전송 실패");
                Log.v("테스트","폼 전송 실패");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //toastMessage("폼 전송 완료");
                //Log.v("테스트","받은 폼 : "+response.body().string());
                isFinish=true;

                String res=response.body().string();
                try{
                    JSONArray jsonArray=new JSONArray(res);
                    Gson gson=new Gson();
                    int len=(jsonArray.length()>3) ? 3:jsonArray.length();
                    for(int i=0;i<len;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        FormDTO formDTO=gson.fromJson(jsonObject.getString("json"),FormDTO.class);
                        UploadedSurveyDTO uploadedSurveyDTO=new UploadedSurveyDTO();
                        uploadedSurveyDTO.set_id(jsonObject.getInt("_id"));
                        uploadedSurveyDTO.setTitle(formDTO.getTitle());
                        uploadedSurveyDTO.setResponse_cnt(jsonObject.getInt("response_cnt"));
                        uploadedSurveyDTO.setTime(getTime(jsonObject.getString("time")));
                        datas.add(uploadedSurveyDTO);
                    }
                    Log.v("테스트",datas.size()+"");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadedSurveyAdapter=new UploadedSurveyRV(getContext(),userEmail,datas);
                            responseWaitSurveyRecycleView.setAdapter(uploadedSurveyAdapter);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("테스트","받은 폼 error: "+e.getMessage());
                }

            }
        });
    }
}
