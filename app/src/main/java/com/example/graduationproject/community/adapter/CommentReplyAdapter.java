package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.PostActivity;
import com.example.graduationproject.community.model.CommentReplyDTO;
import com.example.graduationproject.community.model.OnItemClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyHolder>{
    private ArrayList<CommentReplyDTO> items;
    private Context mContext;
    private View itemView;
    private OnItemClick mCallback;

    public CommentReplyAdapter(Context context, OnItemClick callback){
        this.mContext = context;
        this.items = new ArrayList<>();
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public CommentReplyAdapter.CommentReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_comment_reply_item,parent,false);
        return new CommentReplyAdapter.CommentReplyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentReplyAdapter.CommentReplyHolder holder, int position) {
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

        String targetEmailText = "@" + items.get(position).getTargetUserEmail().split("@")[0];
        String contentText = targetEmailText + "   " + items.get(position).getContent();
        SpannableString spannableString = new SpannableString(contentText);
        int start = contentText.indexOf(targetEmailText);
        int end = start + targetEmailText.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(spannableString);

        holder.replyImage.setImageResource(R.drawable.ic_enter_78501);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<CommentReplyDTO> datas){
        this.items.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
    }

    class CommentReplyHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        ImageView replyImage;
        TextView userEmail;
        TextView time;
        TextView content;
        Button delButton;
        LinearLayout replyObject;

        CommentReplyHolder(View view){
            super(view);
            profileImage=(ImageView)view.findViewById(R.id.commentReply_profile);
            replyImage = (ImageView)view.findViewById(R.id.reply_image);
            userEmail=(TextView)view.findViewById(R.id.commentReply_id);
            time=(TextView)view.findViewById(R.id.commentReply_time);
            content=(TextView)view.findViewById(R.id.commentReply_text);
            delButton=(Button)view.findViewById(R.id.commentReply_del);
            replyObject = (LinearLayout)view.findViewById(R.id.commentReply_object);

            replyObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).getPost_id(), PostActivity.COMMENT_REPLY);
                    mCallback.getTargetUserEmail(items.get(getAdapterPosition()).getUserEmail());
                }
            });

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onCommentDelClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT_REPLY);
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();
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
}
