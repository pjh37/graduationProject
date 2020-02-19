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
import com.example.graduationproject.login.LoginSession;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{
    private ArrayList<FriendDTO> items=new ArrayList<>();
    private Context mContext;
    public FriendAdapter(Context context, ArrayList<FriendDTO> items){
        this.mContext=context;
        this.items=items;
    }
    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_item,parent,false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        if(items.get(position).getProfileImageUrl()==null){
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(items.get(position).getProfileImageUrl()))
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.custom_item)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }
        holder.btnFriendRequest.setOnClickListener(new ClickListener(items.get(position).getUserEmail()));
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
        Button btnFriendRequest;
        FriendHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
            userEmail=(TextView)itemView.findViewById(R.id.userEmail);
            btnFriendRequest=(Button)itemView.findViewById(R.id.btnFriendRequest);
        }
    }
    class ClickListener implements View.OnClickListener{
        private String to;
        public ClickListener(String to){
            this.to=to;
        }
        @Override
        public void onClick(View view) {
            HashMap<String, Object> input = new HashMap<>();
            input.put("from", LoginSession.getUserEmail());
            input.put("to",to);

            RetrofitApi.getService().friendRequest(input).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) { }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) { }
            });
        }
    }
}
