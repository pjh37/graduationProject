package com.example.graduationproject.community.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.ChatRoomAdapter;
import com.example.graduationproject.community.adapter.SearchAdapter;
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.FriendDTO;

import java.util.ArrayList;

public class ChatServiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChatRoomDTO> datas=new ArrayList<>();

    private Button btnChatRoomCreate;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);
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
        //adapter=new ChatRoomAdapter(this,datas);
        //recyclerView.setAdapter(adapter);
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
