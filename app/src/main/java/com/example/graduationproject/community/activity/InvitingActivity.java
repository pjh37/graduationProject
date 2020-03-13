package com.example.graduationproject.community.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.ChatRoomAdapter;
import com.example.graduationproject.community.adapter.FriendAdapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

public class InvitingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FriendAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviting);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        datas=new ArrayList<>();
        adapter=new FriendAdapter(InvitingActivity.this,datas,1);
        getFriendList();
    }

    public void getFriendList(){
        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());
        RetrofitApi.getService().friendSelect(input).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    Log.v("InvitingActivity",datas.get(0).getUserEmail());
                    adapter.addItems(datas);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) { }
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
