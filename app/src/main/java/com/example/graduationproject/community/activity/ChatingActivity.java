package com.example.graduationproject.community.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

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
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.ChatRoomTempDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.mainActivityViwePager.RequestType;
import com.example.graduationproject.messageservice.MessageDTO;
import com.example.graduationproject.messageservice.MessageManager;
import com.example.graduationproject.messageservice.MessageSaveManager;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.Set;

public class ChatingActivity extends AppCompatActivity {
    private static final int COUNT=50;
    private int offset;
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
        offset=0;
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
        recyclerView.addOnScrollListener(new ScrollListener());
        adapter=new ChatingAdapter(this,items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMessageCache(roomKey,offset);
    }
    //로컬 캐시
    public void getMessageCache(String roomKey,int offset){
        ArrayList<MessageDTO> msg=MessageSaveManager.getInstance(this).findRoomMessage(roomKey, offset);
        adapter.addAll(msg);
        Log.v("캐시테스트","로컬에서 읽은것 : "+msg.size());
        getRoomMessages(roomKey,msg.size());
    }
    public void getRoomMessages(String roomKey,int offset){
        RetrofitApi.getService().getRoomMessages(roomKey,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<MessageDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<MessageDTO>> call, Response<ArrayList<MessageDTO>> response) {
                if(response.body()!=null){
                    ArrayList<MessageDTO> msg=response.body();
                    adapter.addAll(msg);
                    Log.v("캐시테스트","외부db에서 읽은것 : "+msg.size());
                    MessageSaveManager.getInstance(ChatingActivity.this).insertAll(msg);
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MessageDTO>> call, Throwable t) { }
        });
    }
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MessageDTO msg=(MessageDTO)intent.getSerializableExtra("msg");
            if(roomKey.equals(msg.getRoomKey())){
                adapter.addItem(msg);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
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
                    adapter.addItem(msg);
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    editSendMessage.setText("");
                    break;
                }
            }
        }
    }

    public class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView, dx, dy);
            int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

            if(lastVisiblePosition==adapter.getItemCount()-1){
               offset+=50;
               getRoomMessages(roomKey,offset);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
