package com.example.graduationproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.form.FormActivity;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.result.ResultActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class old_UploadedFormEditableActivity extends AppCompatActivity {
    private int form_id;
    private OkHttpClient client;
    private boolean isFinish=false;
    private ProgressBar progressBar;

    private ImageView imgSurveyWriterPhoto;
    private TextView tvSurveyWriterEmail;
    //    private String userEmail;
    private TextView tvSurveyRoomTitle;
    private String SurveyRoomTitle;

    public static final int categoryNumber = 100; // 숫자는 아무의미 없음, 그냥 정한거


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_uploaded_form_editable);

        Intent intent=getIntent();
        form_id=intent.getIntExtra("form_id",-1);
        SurveyRoomTitle = intent.getStringExtra("title");


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        client=new OkHttpClient();

        progressBar=(ProgressBar)findViewById(R.id.progress);

        imgSurveyWriterPhoto = findViewById(R.id.survey_writer_photo);
        Glide.with(this).load(Session.getUserImage_uri()).into(imgSurveyWriterPhoto);
        tvSurveyWriterEmail = findViewById(R.id.survey_writer_email);
        tvSurveyWriterEmail.setText(Session.getUserEmail());
        tvSurveyRoomTitle = findViewById(R.id.survey_room_title);
        tvSurveyRoomTitle.setText(SurveyRoomTitle);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnResult: {
                resultRequest();
//                Toast.makeText(getApplicationContext(),"btnResult",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnShare: {
                shareRequest();
//                if(isActive){
//                    shareRequest();
//                }else{
//                Toast.makeText(getApplicationContext(),"비활성화 상태입니다.",Toast.LENGTH_SHORT).show();
//                }

                break;
            }
            case R.id.btnEdit: {
                editRequest();
//                Toast.makeText(getApplicationContext(),"btnEdit",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnPreview: {
                previewRequest();
//                Toast.makeText(getApplicationContext(),"btnPreview",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnDelete: {
                deleteRequest();
//                Toast.makeText(getApplicationContext(),"btnDelete",Toast.LENGTH_SHORT).show();
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
                if(res.equals("load error")){
                    Toast.makeText(getApplicationContext(),"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(old_UploadedFormEditableActivity.this, FormActivity.class);
                    intent.putExtra("form_id",form_id);
                    intent.putExtra("json",res);
                    intent.putExtra("category", categoryNumber);
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
//                .url(getString(R.string.baseUrl)+"deleteform/"+form_id)
                .url(getString(R.string.baseUrl) + "deleteform/" + form_id+"/"+Session.getUserEmail())
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
                if(res.equals("delete error")){
                    Toast.makeText(getApplicationContext(),"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
            }
        });
    }
    public void resultRequest(){
        Intent intent =new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("form_id",form_id);
//        intent.putExtra("userEmail",userEmail);
        startActivity(intent);
    }
    public void shareRequest(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.baseUrl)+"survey/"+form_id);
        Intent chooser=Intent.createChooser(intent,"공유");
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooser);
    }
    public void previewRequest(){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.baseUrl)+"survey/"+form_id));
        startActivity(intent);
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

    protected void onRestart() {
        // 편집후 바로 메인에 와야 수정사항이 refresh 되기 때문에
        // old_UploadedFormEditableActivity 액티비티는 바로 꺼야함
        super.onRestart();
//        Log.d("mawang", "old_UploadedFormEditableActivity onRestart 실행");
        finish();
    }

}
