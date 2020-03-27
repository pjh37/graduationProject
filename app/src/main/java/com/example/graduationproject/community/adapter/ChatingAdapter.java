package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.messageservice.MessageDTO;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatingAdapter extends RecyclerView.Adapter<ChatingAdapter.ChatingHolder>{
    private final ArrayList<MessageDTO> items;
    private Context mContext;
    private View itemView;

    public ChatingAdapter(Context context, ArrayList<MessageDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public ChatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_chating_item,parent,false);
        return new ChatingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatingHolder holder, int position) {
        MessageDTO msg=items.get(position);
        //자신의 메세지
        if(Session.getUserEmail().equals(msg.getUserEmail())){
            holder.rightView.setVisibility(View.GONE);
            holder.leftView.setVisibility(View.VISIBLE);
            holder.profileImage.setVisibility(View.GONE);
            holder.txtRightDate.setVisibility(View.GONE);
            holder.txtMessage.setText(msg.getMessage());
            holder.txtMessage.setBackgroundResource(R.drawable.outbox2);
            holder.txtLeftDate.setVisibility(View.VISIBLE);
            holder.txtLeftDate.setText(msg.getDate());
            holder.txtUserEmail.setVisibility(View.GONE);
        }else{
            //다른 사람의 메세지
            holder.rightView.setVisibility(View.VISIBLE);
            holder.leftView.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(mContext.getString(R.string.baseUrl)+"user/profile/"+items.get(position).getUserEmail()+".jpg")
                    .error(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
            holder.txtRightDate.setVisibility(View.VISIBLE);
            holder.txtRightDate.setText(msg.getDate());
            holder.txtMessage.setText(msg.getMessage());
            holder.txtMessage.setBackgroundResource(R.drawable.inbox2);
            holder.txtLeftDate.setVisibility(View.GONE);
            holder.txtUserEmail.setText(msg.getUserEmail().split("@")[0]);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MessageDTO msg){
        items.add(msg);
        notifyItemInserted(items.size());
        //notifyDataSetChanged();
    }
    public void addAll(ArrayList<MessageDTO> msg){
        items.addAll(items.size(),msg);
        notifyDataSetChanged();
    }
    class ChatingHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView txtUserEmail;
        TextView txtMessage;
        TextView txtLeftDate;
        TextView txtRightDate;
        View leftView;
        View rightView;
        ChatingHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            txtUserEmail=(TextView)itemView.findViewById(R.id.txtUserEmail);
            txtMessage=(TextView)itemView.findViewById(R.id.txtMessage);
            txtLeftDate=(TextView)itemView.findViewById(R.id.txtLeftDate);
            txtRightDate=(TextView)itemView.findViewById(R.id.txtRightDate);
            leftView=(View)itemView.findViewById(R.id.leftView);
            rightView=(View)itemView.findViewById(R.id.rightView);
        }
    }
}
