package com.example.graduationproject.community.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.ChatRoomAdapter;
import com.example.graduationproject.community.adapter.SearchAdapter;
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.ChatRoomTempDTO;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.messageservice.MessageManager;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ChatServiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChatRoomDTO> datas;
    private HashMap<String,ArrayList<String>> rooms;
    private Button btnChatRoomCreate;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);
        //채팅 서버와 연결 테스트
        MessageManager.getInstance(this).connect();
        rooms=new HashMap<>();
        datas=new ArrayList<>();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnChatRoomCreate=(Button)findViewById(R.id.btnChatRoomCreate);
        btnChatRoomCreate.setOnClickListener(new ClickListener());
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ChatRoomAdapter(this,datas);
        recyclerView.setAdapter(adapter);
        getChatRoomList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnChatRoomCreate:{
                    Intent intent =new Intent(ChatServiceActivity.this,InvitingActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }
    public void getChatRoomList(){
        String userEmail= Session.getUserEmail();

        RetrofitApi.getService().getRoomList(userEmail).enqueue(new retrofit2.Callback<ArrayList<ChatRoomTempDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<ChatRoomTempDTO>> call, Response<ArrayList<ChatRoomTempDTO>> response) {
                if(response.body()!=null){
                    ArrayList<ChatRoomTempDTO> res=response.body();

                    for(int i=0;i<res.size();i++){
                        if(rooms.containsKey(res.get(i).getRoomKey())){
                            rooms.get(res.get(i).getRoomKey()).add(res.get(i).getUserEmail());
                        }else{
                            ArrayList<String> userEmails=new ArrayList<>();
                            userEmails.add(res.get(i).getUserEmail());
                            rooms.put(res.get(i).getRoomKey(),userEmails);
                        }
                    }
                    Set<String> roomKeys=rooms.keySet();
                    for(String roomKey:roomKeys){
                        ChatRoomDTO room=new ChatRoomDTO();
                        room.setRoomKey(roomKey);
                        room.setUserCnt(String.valueOf(rooms.get(roomKey).size()));
                        room.setUserEmails(rooms.get(roomKey));
                        room.setChatRoomImageUrl(rooms.get(roomKey).get(0));
                        Log.v("테스트","roomKeys"+room.getUserEmails());
                        datas.add(room);
                    }
                    Log.v("테스트","datas "+datas.size());
                    adapter.addData(datas);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<ChatRoomTempDTO>> call, Throwable t) { }
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
