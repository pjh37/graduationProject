package com.example.graduationproject.community.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

import android.os.Bundle;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.GroupAdapter;
import com.example.graduationproject.community.adapter.MyGroupAdapter;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

public class MyGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyGroupAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupDTO> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        adapter=new MyGroupAdapter(this,datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getMyGroup();
    }
    public void getMyGroup(){
        RetrofitApi.getService().getMyGroup(Session.getUserEmail()).enqueue(new retrofit2.Callback<ArrayList<GroupDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<GroupDTO>> call, Response<ArrayList<GroupDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<GroupDTO>> call, Throwable t) { }
        });
    }
}
