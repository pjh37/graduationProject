package com.example.graduationproject.community.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.MyGroupAdapter;
import com.example.graduationproject.community.adapter.PostAdapter;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.community.model.PostDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{
    private final static Integer COUNT=20;
    Integer offset=0;
    private Integer groupID;

    ImageView cover;
    ImageView profileImage;
    TextView userEmail;
    EditText content;
    Button btnPost;

    RecyclerView recyclerView;
    PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostDTO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        groupID=getIntent().getIntExtra("groupID",-1);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        adapter=new PostAdapter(this,datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        cover=(ImageView)findViewById(R.id.cover);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        userEmail=(TextView)findViewById(R.id.userEmail);
        btnPost=(Button)findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

        content=(EditText) findViewById(R.id.content);
        content.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    content.setHeight(300);
                }else{
                    content.setHeight(100);
                }
            }
        });
        load();
    }
    public void load(){
        Glide.with(this).load(getString(R.string.baseUrl)+"group/image/cover/"+groupID)
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(10)))
                .into(cover);
        Glide.with(this).load(getString(R.string.baseUrl)+"user/profile/select/"+ Session.getUserEmail() +".jpg")
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(30)))
                .into(profileImage);
        RetrofitApi.getService().getPost(groupID,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<PostDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<PostDTO>> call, Response<ArrayList<PostDTO>> response) {
                if(response.body()!=null){
                    //Log.v("포스트",response.body().get(0).getContent());
                    datas=response.body();
                    adapter.addItems(datas);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PostDTO>> call, Throwable t) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:{
                postCreate();
                content.setText("");
                break;
            }
        }
    }
    public void postCreate(){
        Log.v("포스트","postCreate");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("group_id",groupID);
        hashMap.put("content",content.getText().toString());
        hashMap.put("userEmail",Session.getUserEmail());
        hashMap.put("time",System.currentTimeMillis());
        RetrofitApi.getService().postCreate(hashMap).enqueue(new retrofit2.Callback<Boolean>(){
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {


            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });
    }
}
