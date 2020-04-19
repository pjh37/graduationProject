package com.example.graduationproject.community.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
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
    private Context mContext;
    private View itemView;
    AlertDialog alertDialog;
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
        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"post/image/thumbnail/"+items.get(position).get_id())
                .into(holder.thumbnail);
        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());
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
                    Toast.makeText(mContext,"업데이트 클릭",Toast.LENGTH_LONG).show();
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
                    notifyItemRemoved(position);
                    items.remove(position);

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
        TextView postDelete;
        TextView postUpdate;
        PostHolder(View v){
            super(v);
            profileImage=(ImageView)v.findViewById(R.id.profile_image);
            thumbnail=(ImageView)v.findViewById(R.id.thumbnail);
            userEmail=(TextView)v.findViewById(R.id.userEmail);
            time=(TextView)v.findViewById(R.id.time);
            content=(TextView)v.findViewById(R.id.content);
            postDelete=(TextView)v.findViewById(R.id.post_delete);
            postUpdate=(TextView)v.findViewById(R.id.post_update);
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
