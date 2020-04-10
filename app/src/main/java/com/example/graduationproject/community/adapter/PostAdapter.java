package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.community.model.PostDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.PostHolder>{

    private ArrayList<PostDTO> items;
    private Context mContext;
    private View itemView;

    public PostAdapter(Context context, ArrayList<PostDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_post_item,parent,false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        if(items.get(position).getUserEmail()==null){
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmail()+".jpg")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }
        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<PostDTO> datas){
        int position=items.size();
        this.items.addAll(position,datas);
        notifyItemRangeChanged(position,datas.size());
    }

    class PostHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView userEmail;
        TextView time;
        TextView content;
        PostHolder(View v){
            super(v);
            profileImage=(ImageView)v.findViewById(R.id.profile_image);
            userEmail=(TextView)v.findViewById(R.id.userEmail);
            time=(TextView)v.findViewById(R.id.time);
            content=(TextView)v.findViewById(R.id.content);
        }
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        String time = simpleDate.format(date);
        return time;
    }
}
