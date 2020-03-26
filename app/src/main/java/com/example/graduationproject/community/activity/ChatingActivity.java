package com.example.graduationproject.community.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.ChatRoomAdapter;
import com.example.graduationproject.community.adapter.ChatingAdapter;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.messageservice.MessageDTO;
import com.example.graduationproject.messageservice.MessageManager;

import java.util.ArrayList;

public class ChatingActivity extends AppCompatActivity {
    Button btnSend;
    String roomKey;
    String msg;

    EditText editSendMessage;

    RecyclerView recyclerView;
    ChatingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MessageDTO> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chating);
        registerReceiver(receiver,new IntentFilter("com.example.RECEIVE_ACTION"));

        items=new ArrayList<>();

        Intent intent=getIntent();
        roomKey=intent.getStringExtra("roomKey");
        Log.v("테스트","roomKeys"+roomKey);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new ClickListener());
        editSendMessage=(EditText)findViewById(R.id.editSendMessage);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ChatingAdapter(this,items);
        recyclerView.setAdapter(adapter);
    }
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MessageDTO msg=(MessageDTO)intent.getSerializableExtra("msg");
            if(roomKey.equals(msg.getRoomKey())){
                adapter.addItem(msg);
            }
        }
    };
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSend:{
                    MessageDTO msg=new MessageDTO(roomKey
                            , Session.getUserEmail()
                            ,editSendMessage.getText().toString()
                            ,Session.getTime());
                    MessageManager.msgSend(msg);
                    //adapter.addItem(msg);
                    editSendMessage.setText("");
                    break;
                }
            }
        }
    }
}
