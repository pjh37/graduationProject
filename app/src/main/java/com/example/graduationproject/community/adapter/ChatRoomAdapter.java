package com.example.graduationproject.community.adapter;

import android.content.Context;
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
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.FriendDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder>{
    private ArrayList<ChatRoomDTO> items;
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
        Glide.with(holder.itemView.getContext())
                .load(mContext.getString(R.string.baseUrl)+"user/profile/"+items.get(position).getUserEmails().get(0)+".jpg")
                .error(R.drawable.profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(new RequestOptions().circleCrop()).into(holder.chatRoomImage);
        int len = items.get(position).getUserEmails().size()>3 ? 3 : items.get(position).getUserEmails().size();

        for(int i=0;i<len-1;i++){
            holder.userEmails.append(items.get(position).getUserEmails().get(i)+",");
        }
        holder.userEmails.append(items.get(position).getUserEmails().get(len-1));
        holder.userCnt.setText(items.get(position).getUserCnt());
        holder.time.setText(getTime(items.get(position).getTime()));
        holder.lastReceivedMessage.setText("구현중...");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<ChatRoomDTO> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder{
        ImageView chatRoomImage;
        TextView userEmails;
        TextView userCnt;
        TextView lastReceivedMessage;
        TextView time;

        ChatRoomHolder(View itemView){
            super(itemView);
            chatRoomImage=(ImageView)itemView.findViewById(R.id.chatRoom_image);
            userEmails=(TextView)itemView.findViewById(R.id.userEmails);
            userCnt=(TextView)itemView.findViewById(R.id.userCnt);
            time=(TextView)itemView.findViewById(R.id.userEmail);
            lastReceivedMessage=(TextView)itemView.findViewById(R.id.lastReceivedMessage);
        }
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
}
