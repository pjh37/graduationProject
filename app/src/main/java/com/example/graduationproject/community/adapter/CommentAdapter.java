package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.PostActivity;
import com.example.graduationproject.community.model.CommentDTO;
import com.example.graduationproject.community.model.CommentReplyDTO;
import com.example.graduationproject.community.model.OnItemClick;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<CommentDTO> items;
    private Context mContext;
    private View itemView;
//    private boolean isDeleted = false;
    private OnItemClick mCallback;

    private final static int COUNT = 5;
    private RecyclerView.LayoutManager layoutManager;
    Integer offset=0;

    public CommentAdapter(Context context, OnItemClick callback){
        this.mContext = context;
        this.items = new ArrayList<>();
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_comment_item,parent,false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
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
//        holder.userEmail.setText(items.get(position).getNickname());

        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());

        loadCommentReply(holder.adapter, items.get(holder.getAdapterPosition()).get_id());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<CommentDTO> datas){
        this.items.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView userEmail;
        TextView time;
        TextView content;

        //        Button delButton;
        TextView delButton;

        RecyclerView replyRV;
        CommentReplyAdapter adapter;

        LinearLayout commentObject;

        CommentHolder(View view){
            super(view);
            profileImage=(ImageView)view.findViewById(R.id.comment_profile);
            userEmail=(TextView)view.findViewById(R.id.comment_id);
            time=(TextView)view.findViewById(R.id.comment_time);
            content=(TextView)view.findViewById(R.id.comment_text);

            delButton=view.findViewById(R.id.comment_del);

            commentObject = (LinearLayout)view.findViewById(R.id.comment_object);
            replyRV = (RecyclerView)view.findViewById(R.id.commentReply_rv);

            layoutManager = new LinearLayoutManager(mContext);
            replyRV.setLayoutManager(layoutManager);

            adapter = new CommentReplyAdapter(mContext, mCallback);
            replyRV.setAdapter(adapter);

            commentObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT_REPLY);
                    mCallback.setTargetUserEmail(items.get(getAdapterPosition()).getUserEmail());
                    mCallback.setTargetNickname(items.get(getAdapterPosition()).getNickname());
                }
            });

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onCommentDelClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT);
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

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

    public void loadCommentReply(CommentReplyAdapter adapter, int post_id){
        RetrofitApi.getService().getReply(post_id,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<CommentReplyDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<CommentReplyDTO>> call, Response<ArrayList<CommentReplyDTO>> response) {
                if(response.body()!=null){
                    if(response.body()!=null){
                        ArrayList<CommentReplyDTO> data=response.body();
                        adapter.clear();
                        adapter.addItems(data);
                        Log.d("CommentLog",Integer.toString(adapter.getItemCount()));
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CommentReplyDTO>> call, Throwable t) { }
        });
    }

}
