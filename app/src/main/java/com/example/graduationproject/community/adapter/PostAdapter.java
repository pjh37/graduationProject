package com.example.graduationproject.community.adapter;

import android.app.AlertDialog;
import android.content.Context;

import android.util.Log;

import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.graduationproject.community.activity.PostUpdateActivity;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.community.model.PostDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.example.graduationproject.retrofitinterface.RetrofitResponse;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    AlertDialog alertDialog;

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
        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"post/image/thumbnail/"+items.get(position).get_id())
                .into(holder.thumbnail);

//        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.userEmail.setText(items.get(position).getNickname()); // 이거 닉넴으로 바꾼다.

        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());

        loadComment(holder.adapter, items.get(holder.getAdapterPosition()).get_id());

        holder.postDelete.setOnClickListener(new ClickListener(holder,position));
        holder.postUpdate.setOnClickListener(new ClickListener(holder,position));

        if(items.get(position).getUserEmail().equals(Session.getUserEmail())){
            holder.postDelete.setVisibility(View.VISIBLE);
            holder.postUpdate.setVisibility(View.VISIBLE);
        }else{
            holder.postDelete.setVisibility(View.GONE);
            holder.postUpdate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<PostDTO> datas){
        this.items.addAll(0,datas);
        notifyDataSetChanged();
    }
    public void addItem(PostDTO postDTO){
        this.items.add(0,postDTO);
        notifyItemInserted(0);
    }
    class ClickListener implements View.OnClickListener{
        PostHolder holder;
        int position;
        ClickListener(@NonNull PostHolder holder, int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_delete:{
                    postDeleteDialog(position);
                    break;
                }
                case R.id.post_update:{
                    Intent intent=new Intent(mContext, PostUpdateActivity.class);
                    intent.putExtra("postID",items.get(position).get_id());
                    intent.putExtra("content",items.get(position).getContent());
                    mContext.startActivity(intent);
                    break;
                }
            }
        }
    }
    public void postDeleteDialog(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postDelete(position);
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void postDelete(int position){
        RetrofitApi.getService().postDelete(items.get(position).get_id()).enqueue(new retrofit2.Callback<RetrofitResponse>(){
            @Override
            public void onResponse(Call<RetrofitResponse> call, @NonNull Response<RetrofitResponse> response) {
                if(response.body().isRight()){
                    items.remove(position);
                    notifyItemRemoved(position);

                }
            }
            @Override
            public void onFailure(Call<RetrofitResponse> call, Throwable t) { }
        });
    }
    class PostHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        ImageView thumbnail;
        TextView userEmail;
        TextView time;
        TextView content;
        Button commentBtn;
        ConstraintLayout postObject;
        RecyclerView commentRV;
        CommentAdapter adapter;

        TextView postDelete;
        TextView postUpdate;

        PostHolder(View v){
            super(v);
            profileImage=(ImageView)v.findViewById(R.id.profile_image);
            thumbnail=(ImageView)v.findViewById(R.id.thumbnail);
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

                    if(commentRV.getVisibility()== View.VISIBLE){
                        commentRV.setVisibility(View.GONE);
                        commentBtn.setText("댓글 보기");
                    }else{
                        commentRV.setVisibility(View.VISIBLE);
                        commentBtn.setText("댓글 닫기");
                    }
                }
            });

            postDelete=(TextView)v.findViewById(R.id.post_delete);
            postUpdate=(TextView)v.findViewById(R.id.post_update);

        }

    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
