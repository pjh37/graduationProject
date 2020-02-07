package com.example.graduationproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.form.FormActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UploadedFormEditableActivity extends AppCompatActivity {
    private int form_id;
    private Switch mStatusSwitch;
    private boolean isFinish=false;
    private ProgressBar progressBar;
    private TextView txtStatus;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_form_editable);

        Intent intent=getIntent();
        form_id=intent.getIntExtra("form_id",-1);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        client=new OkHttpClient();

        progressBar=(ProgressBar)findViewById(R.id.progress);
        mStatusSwitch=(Switch)findViewById(R.id.status_switch);
        mStatusSwitch.setChecked(true);
        txtStatus=(TextView)findViewById(R.id.txtStatus);
        txtStatus.setTextColor(Color.GREEN);

        mStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    txtStatus.setText("ACTIVE");
                    txtStatus.setTextColor(Color.GREEN);
                }else{
                    txtStatus.setText("INACTIVE");
                    txtStatus.setTextColor(Color.RED);
                }
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnEdit:{
                //Intent intent=new Intent(this, FormActivity.class);
                editRequest();
                break;
            }
            case R.id.btnPreview:{
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.baseUrl)+"survey/"+form_id));
                startActivity(intent);
                break;
            }
            case R.id.btnDelete:{
                deleteRequest();
                break;
            }
        }
    }
    public void editRequest(){

        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(getString(R.string.baseUrl)+"load/"+form_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                if(res.equals("error")){
                    Toast.makeText(getApplicationContext(),"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(UploadedFormEditableActivity.this, FormActivity.class);
                    intent.putExtra("form_id",form_id);
                    intent.putExtra("json",res);
                    startActivity(intent);
                }
            }
        });
    }
    public void deleteRequest(){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
            }
        }).start();

        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(getString(R.string.baseUrl)+"deleteform/"+form_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getApplicationContext(),"요청 실패",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                isFinish=true;
                if(res.equals("error")){
                    Toast.makeText(getApplicationContext(),"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
            }
        });
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
