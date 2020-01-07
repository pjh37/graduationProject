package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.form.BaseFormActivity;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.offlineform.OfflineFormActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    RecyclerView uploadedSurveyRV;
    RecyclerView.Adapter  uploadedSurveyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UploadedSurveyDTO> datas;
    public  String userEmail;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url=getString(R.string.baseUrl);
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");
        drawerLayout=(DrawerLayout)findViewById(R.id.main_drawer);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        datas=new ArrayList<>();
        uploadedSurveyRV=(RecyclerView)findViewById(R.id.response_wait_list);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        uploadedSurveyRV.addItemDecoration(new ItemDecorate());
        uploadedSurveyRV.setLayoutManager(layoutManager);

        getMySurveyList(userEmail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                TextView txtUserID=(TextView)findViewById(R.id.txtUserID);
                txtUserID.setText(userEmail);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()){
                    case R.id.offline: {
                        Intent intent = new Intent(getApplicationContext(), OfflineFormActivity.class);
                        intent.putExtra("userEmail",userEmail);
                        startActivity(intent);
                        break;
                    }
                    case R.id.template: {
                        Toast.makeText(getApplicationContext(), "template", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.notification: {

                    }
                    case R.id.help:{

                    }
                    case R.id.share: {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.baseUrl)+"survey/6");
                        Intent chooser=Intent.createChooser(intent,"공유");
                        startActivity(chooser);
                         break;
                    }
                }
                return true;
            }
        });
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void getMySurveyList(String userEmail){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("userEmail",userEmail)
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
                String res=response.body().string();
                try{
                    JSONArray jsonArray=new JSONArray(res);
                    Gson gson=new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        FormDTO formDTO=gson.fromJson(jsonObject.getString("json"),FormDTO.class);
                        UploadedSurveyDTO uploadedSurveyDTO=new UploadedSurveyDTO();
                        uploadedSurveyDTO.set_id(jsonObject.getInt("_id"));
                        uploadedSurveyDTO.setTitle(formDTO.getTitle());
                        uploadedSurveyDTO.setResponseCnt(jsonObject.getInt("response_cnt"));
                        uploadedSurveyDTO.setTime(getTime(jsonObject.getString("time")));
                        datas.add(uploadedSurveyDTO);
                    }
                    Log.v("테스트",datas.size()+"");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadedSurveyAdapter=new UploadedSurveyRV(getApplicationContext(),userEmail,datas);
                            uploadedSurveyRV.setAdapter(uploadedSurveyAdapter);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("테스트","받은 폼 error: "+e.getMessage());
                }

            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bottom_menu:
                Intent intent=new Intent(this, BaseFormActivity.class);
                intent.putExtra("userEmail",userEmail);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(intent);
                break;
        }
    }
}
