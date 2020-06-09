package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.ChatingActivity;
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.FriendDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder>{
    private final ArrayList<ChatRoomDTO> items;
    private Context mContext;
    private View itemView;

    public ChatRoomAdapter(Context context, ArrayList<ChatRoomDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_chatroom_item,parent,false);
        return new ChatRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        Log.v("테스트","ChatRoomAdapter : "+ items.size());
        Glide.with(holder.itemView.getContext())
                .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmails().get(0)+".jpg")
                .error(R.drawable.profile)
                .apply(new RequestOptions().circleCrop()).into(holder.chatRoomImage);


//        int len = items.get(position).getUserEmails().size()>2 ? 2 : items.get(position).getUserEmails().size();
        int len = items.get(position).getUserNicknames().size();

        holder.userEmails.setText(""); // 한 번 비워주고
        // text view 길이로 '...' 붙여야겠다.
        if (len > 4){
            for(int i=0;i<4;i++){ // 4개까지만 표시하고
//                holder.userEmails.append(items.get(position).getUserEmails().get(i).split("@")[0]+",");
                holder.userEmails.append(items.get(position).getUserNicknames().get(i) +",");
            }
            holder.userEmails.append("..."); // 점 표시
        }else{ // 4개 이하면
            for(int i=0;i<len-1;i++){
//                holder.userEmails.append(items.get(position).getUserEmails().get(i).split("@")[0]+",");
                holder.userEmails.append(items.get(position).getUserNicknames().get(i) +",");
            }
//            holder.userEmails.append(items.get(position).getUserEmails().get(len-1).split("@")[0]);
            holder.userEmails.append(items.get(position).getUserNicknames().get(len-1)); // 마지막 참여자
        }

        //holder.userEmails.append(items.get(position).getUserEmails().get(len-1).split("@")[0]);


        holder.userCnt.setText("참여 "+items.get(position).getUserCnt()+"명");

//        holder.time.setText(getTime(items.get(position).getTime()));
        holder.time.setText("");

        holder.lastReceivedMessage.setText("지난 문자");

        itemView.setOnClickListener(new ClickListener(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(ArrayList<ChatRoomDTO> data){
        //this.items.addAll(data); // ChatServiceActivity 에서
        // adapter=new ChatRoomAdapter(this,items); 로 연결되었기 때문에 하면 x2 가된다..
        notifyDataSetChanged();
    }
    public void datasClear() { // 삭제 즉각 반영
        items.clear();
        notifyDataSetChanged();
//        Log.d("mawang", "ChatRoomAdapter datasClear - called");
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder{
        ImageView chatRoomImage;
        TextView userEmails; // userNicknames
        TextView userCnt;
        TextView lastReceivedMessage;
        TextView time;

        ChatRoomHolder(View itemView){
            super(itemView);
            chatRoomImage=(ImageView)itemView.findViewById(R.id.chatRoom_image);
            userEmails=(TextView)itemView.findViewById(R.id.userEmails);
            userCnt=(TextView)itemView.findViewById(R.id.userCnt);
            time=(TextView)itemView.findViewById(R.id.date);
            lastReceivedMessage=(TextView)itemView.findViewById(R.id.lastReceivedMessage);

        }
    }
    class ClickListener implements View.OnClickListener{
        int pos;
        ClickListener(int pos){
            this.pos=pos;
        }
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext, ChatingActivity.class);
            intent.putExtra("roomKey",items.get(pos).getRoomKey());
            mContext.startActivity(intent);
        }
    }
    public String getTime(String str){
        //long now=Long.valueOf(str);
        //임시
        long now=System.currentTimeMillis();
        Date date=new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
}
