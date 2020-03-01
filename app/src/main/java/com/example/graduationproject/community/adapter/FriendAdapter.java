package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.FriendDTO;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{
    private ArrayList<FriendDTO> items=new ArrayList<>();
    private Context mContext;
    private View itemView;
    public FriendAdapter(Context context, ArrayList<FriendDTO> items){
        this.mContext=context;
        this.items=items;
    }
    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_item,parent,false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        if(items.get(position).getProfileImageUrl()==null){
            Glide.with(mContext)
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(mContext)
                    .load(mContext.getString(R.string.baseUrl)+"user/profile/"+items.get(position).getProfileImageUrl()+".jpg")
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }

        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(ArrayList<FriendDTO> items){
        this.items=items;
        notifyDataSetChanged();
    }
    public void addItems(ArrayList<FriendDTO> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    class FriendHolder extends RecyclerView.ViewHolder{
        TextView userEmail;
        ImageView profileImage;
        Button btnFriendDelete;
        FriendHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
            userEmail=(TextView)itemView.findViewById(R.id.userEmail);
            btnFriendDelete=(Button)itemView.findViewById(R.id.btnFriendDelete);
        }
    }
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }
}
