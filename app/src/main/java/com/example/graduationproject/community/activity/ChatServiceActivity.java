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
    private ArrayList<ChatRoomDTO> items;
    private HashMap<String,ArrayList<String>> rooms;
    private HashMap<String,ArrayList<String>> roomsUserEmails;
    private Button btnChatRoomCreate;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);
        //채팅 서버와 연결 테스트
        MessageManager.getInstance(this).connect();
        rooms=new HashMap<>();
        roomsUserEmails=new HashMap<>();
        items=new ArrayList<>();
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
        adapter=new ChatRoomAdapter(this,items);
        recyclerView.setAdapter(adapter);

        //        getChatRoomList(); // onResume 으로 옮김
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
                    for(ChatRoomTempDTO t : res){
                        //Log.d("채팅방리스트","ChatRoomTempDTO : "+t.getUserEmail()+" "+t.getUserNickname()+" "+t.getRoomKey());
                    }
//                    ArrayList<ChatRoomDTO> datas=new ArrayList<>();
                    for(int i=0;i<res.size();i++){
                        if(rooms.containsKey(res.get(i).getRoomKey())){
                            // 채팅방에 참여자 추가
                            roomsUserEmails.get(res.get(i).getRoomKey()).add(res.get(i).getUserEmail());
                            rooms.get(res.get(i).getRoomKey()).add(res.get(i).getUserNickname());
                        }else{
                            ArrayList<String> userEmails=new ArrayList<>();
                            userEmails.add(res.get(i).getUserEmail());
                            ArrayList<String> userNicks=new ArrayList<>();
                            userNicks.add(res.get(i).getUserNickname());

                            // 채팅방 개설 ,채팅방 방장 추가
//                            rooms.put(res.get(i).getRoomKey(),userEmails);
                            roomsUserEmails.put(res.get(i).getRoomKey(),userEmails);
                            rooms.put(res.get(i).getRoomKey(),userNicks);
                        }
                    }
                    Set<String> roomKeys=rooms.keySet();
                    for(String roomKey:roomKeys){
                        ChatRoomDTO room=new ChatRoomDTO();
                        room.setRoomKey(roomKey);
                        room.setUserCnt(String.valueOf(rooms.get(roomKey).size()));

                        room.setUserEmails(roomsUserEmails.get(roomKey)); // ArrayList , 참여자들 메일
                        room.setUserNicknames(rooms.get(roomKey)); // ArrayList , 참여자들 닉네임

                        room.setChatRoomImageUrl(rooms.get(roomKey).get(0));

                        //                        datas.add(room);
                        items.add(room);
                    }
                    //                    adapter.addData(datas);
                    for(ChatRoomDTO t : items){
                        Log.d("테스트","ChatRoomDTO : "+t.getUserEmails()+" "+t.getUserNicknames()+" "+t.getRoomKey());
                    }
                    adapter.addData(items);

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

    @Override
    public void onResume() {
        super.onResume();

//        Log.d("mawang","ChatServiceActivity onResume - 짠");
        items.clear();
        rooms.clear();
        adapter.datasClear();


        getChatRoomList(); // 채팅방 불러오기
//        Log.d("mawang", "ChatServiceActivity onResume - called");
    }
}
