package com.example.graduationproject.mainActivityViwePager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.UploadedSurveyRV;
import com.example.graduationproject.form.FormSaveManager;
import com.example.graduationproject.offlineform.FormItem;
import com.example.graduationproject.offlineform.OfflineFormRVAdapter;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class moreViewActivity extends AppCompatActivity {
    private static final int SERVER_SURVEY=0;
    private static final int OFFLINE_SURVEY=1;
    private int type;
    private RecyclerView recyclerView;

    private UploadedSurveyRV surveysAdapter;
    private OfflineFormRVAdapter offlineFormAdapter;

    private ArrayList<UploadedSurveyDTO> serverSurveyDatas;
    private ArrayList<FormItem> offlineSurveyDatas;
    private RecyclerView.LayoutManager layoutManager;

    private FormSaveManager formSaveManager;
    public String userEmail;
    private String url;

    private ProgressBar progressBar;
    private boolean isFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_view);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        serverSurveyDatas=new ArrayList<>();
        offlineSurveyDatas=new ArrayList<>();
        formSaveManager=FormSaveManager.getInstance(this);
        url=getString(R.string.baseUrl);
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");
        type=intent.getIntExtra("type",-1);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        layoutManager=new LinearLayoutManager(this);
        surveysAdapter=new UploadedSurveyRV(this,userEmail,serverSurveyDatas);
        offlineFormAdapter=new OfflineFormRVAdapter(this,offlineSurveyDatas,userEmail);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,1));

        load(type);
    }
    public void load(int type){
        switch (type){
            case SERVER_SURVEY:{
                recyclerView.setAdapter(surveysAdapter);
                getMySurveyList();
                break; }
            case OFFLINE_SURVEY:{
                recyclerView.setAdapter(offlineFormAdapter);
                new LoadTask().execute();
                break;
            }
        }
    }
    public void getMySurveyList(){
        Log.v("테스트","getMySurveyList 호출");
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
                if(isFinish){
                    runOnUiThread(()->{ progressBar.setVisibility(View.GONE); });
                }

            }
        }).start();
        RetrofitApi.getService().getSurveyList(userEmail).enqueue(new retrofit2.Callback<ArrayList<UploadedSurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, retrofit2.Response<ArrayList<UploadedSurveyDTO>> response) {
                isFinish=true;
                if(response.isSuccessful()){
                    surveysAdapter.addItem(response.body());
                    isFinish=true;
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, Throwable t) { }
        });
    }
    public class LoadTask extends AsyncTask<Void,FormItem,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("테스트","LoadTask 호출");
            offlineFormAdapter.addItem(offlineSurveyDatas);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
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
                    offlineSurveyDatas.add(item);
                }
                cursor.close();
            }
            return null;
        }
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
