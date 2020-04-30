package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.PostActivity;
import com.example.graduationproject.community.model.CommentDTO;
import com.example.graduationproject.community.model.OnItemClick;
import com.example.graduationproject.community.model.PostDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.PostHolder>{

    private ArrayList<PostDTO> items;
    private final static int COUNT = 5;
    private RecyclerView.LayoutManager layoutManager;
    Integer offset=0;

    private Context mContext;
    private View itemView;
    private OnItemClick mCallback;

    public PostAdapter(Context context, ArrayList<PostDTO> items, OnItemClick callback){
        this.mContext=context;
        this.items=items;
        this.mCallback = callback;
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
        loadComment(holder.adapter, items.get(holder.getAdapterPosition()).get_id());
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
        Button commentBtn;
        ConstraintLayout postObject;
        RecyclerView commentRV;
        CommentAdapter adapter;

        PostHolder(View v){
            super(v);
            profileImage=(ImageView)v.findViewById(R.id.profile_image);
            userEmail=(TextView)v.findViewById(R.id.userEmail);
            time=(TextView)v.findViewById(R.id.time);
            content=(TextView)v.findViewById(R.id.content);
            postObject = (ConstraintLayout)v.findViewById(R.id.post_object);
            commentRV = (RecyclerView)v.findViewById(R.id.comment_rv);
            commentBtn = (Button)v.findViewById(R.id.comment_btn);

            layoutManager = new LinearLayoutManager(mContext);
            commentRV.setLayoutManager(layoutManager);

            adapter = new CommentAdapter(mContext, mCallback);
            commentRV.setAdapter(adapter);

            postObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT);
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentRV.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        String time = simpleDate.format(date);
        return time;
    }

    public void loadComment(CommentAdapter adapter, int post_id){
        RetrofitApi.getService().getComment(post_id,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<CommentDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<CommentDTO>> call, Response<ArrayList<CommentDTO>> response) {
                if(response.body()!=null){
                    if(response.body()!=null){
                        ArrayList<CommentDTO> data=response.body();
                        adapter.clear();
                        adapter.addItems(data);
                        Log.d("CommentLog",Integer.toString(adapter.getItemCount()));
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CommentDTO>> call, Throwable t) { }
        });
    }
}
