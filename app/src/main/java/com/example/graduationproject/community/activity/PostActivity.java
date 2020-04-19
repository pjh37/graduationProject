package com.example.graduationproject.community.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Response;

import android.content.Intent;
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
    Button content;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostDTO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        groupID=getIntent().getIntExtra("groupID",-1);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        adapter=new PostAdapter(this,datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        cover=(ImageView)findViewById(R.id.cover);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        userEmail=(TextView)findViewById(R.id.userEmail);

        content=(Button) findViewById(R.id.content);
        content.setOnClickListener(this);
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
                    offset+=datas.size();
                    adapter.addItems(datas);
                    if(datas.size()!=0){
                        Log.v("포스트",datas.size()+"  "+datas.get(0).getContent()+"  offset : "+offset);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PostDTO>> call, Throwable t) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content:{
                Intent intent=new Intent(getApplicationContext(),PostCreateActivity.class);
                intent.putExtra("groupID",groupID);
                startActivity(intent);
                break;
            }
        }
    }

}
