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
import android.view.View;
import android.widget.Button;

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
    private Button btnChatRoomCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviting);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        btnChatRoomCreate=(Button)findViewById(R.id.btnChatRoomCreate);
        btnChatRoomCreate.setOnClickListener(new ClickListener());
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
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnChatRoomCreate:{
                    chatRoomCreateRequest();
                    break;
                }
            }
        }
    }
    public void chatRoomCreateRequest(){
        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());
        input.put("friends",adapter.getCheckedFriends());
        input.put("pk",Session.getUserEmail()+System.currentTimeMillis());

        RetrofitApi.getService().chatRoomCreateRequest(input).enqueue(new retrofit2.Callback<Boolean>(){
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                finish();
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });
    }
}
