package com.example.graduationproject.community.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCreateActivity extends AppCompatActivity{
    private static final int PICK_FROM_ALBUM=1;
    private static final int PUBLIC=0;
    private static final int PRIVATE=1;

    private boolean radioCheck=false;
    private boolean btnPublic;
    private boolean btnPrivate;
    private boolean coverImgCheck=false;

    private Spinner spinner;
    private EditText title;
    private EditText description;
    private EditText groupPassword;
    private RadioGroup radioGroup;
    private Button btnGroupCreate;

    private ImageView coverImage;
    LinearLayout addCoverImage;

    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addCoverImage=(LinearLayout)findViewById(R.id.addCoverImage);
        addCoverImage.setOnClickListener(new ClickListener());

        spinner=(Spinner)findViewById(R.id.categoryMenu);
        title=(EditText)findViewById(R.id.editGroupTitle);
        description=(EditText)findViewById(R.id.editGroupDescription);

        groupPassword=(EditText)findViewById(R.id.groupPassword);
        groupPassword.setVisibility(View.GONE);

        radioGroup=(RadioGroup)findViewById(R.id.btnRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());

        coverImage=(ImageView)findViewById(R.id.coverImage);

        btnGroupCreate=(Button)findViewById(R.id.btnGroupCreate);
        btnGroupCreate.setOnClickListener(new ClickListener());
    }
    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.addCoverImage:{
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);

                    coverImgCheck = true;

                    break;
                }
                case R.id.btnGroupCreate:{
                    Log.v("그룹생성테스트","그룹 만들기 클릭");

                    if(inputCheck()){
                        groupCreate(dataParse());
                        //                        finish(); // 여기서 끝내면 GroupFragment findAllGroup 과 동시에 진행되서 서버 렉 걸림
                    }
                    break;
                }
            }
        }
    }
    public HashMap<String,Object> dataParse(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("title",title.getText().toString());
        map.put("description",description.getText().toString());
        map.put("category",(String)spinner.getSelectedItem());
        map.put("author",Session.getUserEmail());

        if(btnPublic){
            map.put("authority",PUBLIC);
        }else{
            map.put("authority",PRIVATE);
            map.put("groupPassword",groupPassword.getText().toString());
        }
        Log.v("그룹생성테스트","스피너 : "+(String)spinner.getSelectedItem());
        return map;
    }
    public void groupCreate(HashMap<String,Object> map){
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part part = MultipartBody.Part.createFormData(file.getName(), file.getName(), fileReqBody);
        RetrofitApi.getService().groupCreate(map,part).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.v("그룹생성테스트","response");
                finish(); // 생성 후에 GroupFragment 가 findAllGroup 할 수 있게 하자.
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v("groupCreate() fail","failure");
            }
        });
    }

    boolean inputCheck(){
        if(title.getText().toString().trim().length()<2){
             Toast.makeText(this,"1글자 이상 입력하세요",Toast.LENGTH_SHORT).show();
             return false;
        }
        if(!radioCheck){
            Toast.makeText(this,"공개/비공개 여부를 작성하세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spinner.getSelectedItemPosition()==0){
            Toast.makeText(this,"카테고리를 설정하세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(btnPrivate&&groupPassword.getText().toString().equals("")){
            Toast.makeText(this,"비공개일경우 비밀번호필수",Toast.LENGTH_SHORT).show();
            return false;
        }
        // 이미지를 첨부해야 db쪽에서 저장이 된다.
        if(!coverImgCheck){
            Toast.makeText(this,"커버이미지를 첨부하세요.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(R.id.btnPublic==i){
                btnPublic=true;
                btnPrivate=false;
                groupPassword.setVisibility(View.GONE);
            }else if(R.id.btnPrivate==i){
                btnPublic=false;
                btnPrivate=true;
                groupPassword.setVisibility(View.VISIBLE);
            }
            radioCheck=true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            Uri fileUri = data.getData();
            Glide.with(GroupCreateActivity.this).load(fileUri).into(coverImage);
            file=new File(getRealPathFromURI(fileUri));
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getBaseContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
