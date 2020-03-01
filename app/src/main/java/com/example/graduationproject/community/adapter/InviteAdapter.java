package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {
    private ArrayList<FriendDTO> items=new ArrayList<>();
    private Context mContext;
    private View itemView;
    public InviteAdapter(Context context, ArrayList<FriendDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public InviteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_requst_item,parent,false);
        return new InviteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHolder holder, int position) {
        if(items.get(position).getProfileImageUrl()==null){
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(holder.itemView.getContext())
                    .load(mContext.getString(R.string.baseUrl)+"user/profile/"+items.get(position).getProfileImageUrl()+".jpg")
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }
        holder.btnGrant.setOnClickListener(new InviteAdapter.ClickListener(holder,items.get(position).getUserEmail()));
        holder.btnReject.setOnClickListener(new InviteAdapter.ClickListener(holder,items.get(position).getUserEmail()));
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
    class InviteHolder extends RecyclerView.ViewHolder{
        TextView userEmail;
        ImageView profileImage;
        Button btnGrant;
        Button btnReject;
        InviteHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
            userEmail=(TextView)itemView.findViewById(R.id.userEmail);
            btnGrant=(Button)itemView.findViewById(R.id.btnGrant);
            btnReject=(Button)itemView.findViewById(R.id.btnReject);
        }
    }
    class ClickListener implements View.OnClickListener{
        String sender;
        InviteHolder holder;
        ClickListener(@NonNull InviteHolder holder,String sender){
            this.sender=sender;
            this.holder=holder;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGrant:{
                    update(1);
                    holder.userEmail.append(" 님과 친구가 되었습니다.");
                    break;
                }
                case R.id.btnReject:{
                    update(0);
                    holder.userEmail.append(" 님을 거절했습니다.");
                    break;
                }
            }
        }
        void update(int state){
            HashMap<String, Object> input = new HashMap<>();
            input.put("sender", sender);
            input.put("receiver",Session.getUserEmail());
            input.put("state",state);
            RetrofitApi.getService().friendUpdate(input).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) { }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) { }
            });
        }
    }

}
