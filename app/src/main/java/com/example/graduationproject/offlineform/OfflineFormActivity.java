package com.example.graduationproject.offlineform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.form.FormActivity;
import com.example.graduationproject.form.FormSaveManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OfflineFormActivity extends AppCompatActivity {
    RecyclerView offlineForm;
    RecyclerView.Adapter  offlineFormAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FormItem> formItem;
    private FormSaveManager formSaveManager;
    public String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_form);
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");
        formSaveManager=FormSaveManager.getInstance(this);
        formItem=new ArrayList<>();
        offlineForm=(RecyclerView)findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(this);
        offlineForm.addItemDecoration(new DividerItemDecoration(this,1));
        new LoadTask().execute();
    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    public class LoadTask extends AsyncTask<Void,FormItem,Void>{
        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //offlineFormAdapter.notifyDataSetChanged();
            offlineFormAdapter=new OfflineFormRVAdapter(OfflineFormActivity.this,formItem,userEmail);
            offlineForm.setAdapter(offlineFormAdapter);
            offlineForm.setLayoutManager(layoutManager);
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


}
