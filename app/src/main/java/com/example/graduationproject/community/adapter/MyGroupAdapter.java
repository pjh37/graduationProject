package com.example.graduationproject.community.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.PostActivity;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.example.graduationproject.retrofitinterface.RetrofitResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupHolder>{

    private ArrayList<GroupDTO> items;
    private Context mContext;
    private View itemView;
    AlertDialog alertDialog;
    public MyGroupAdapter(Context context, ArrayList<GroupDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public MyGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_group_item,parent,false);
        return new MyGroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupHolder holder, int position) {
        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"group/image/cover/"+items.get(position).get_id())
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(30)))
                .into(holder.cover);
        holder.title.setText(items.get(position).getTitle().substring(1,items.get(position).getTitle().length()-1));
        holder.member_cnt.setText("멤버 : "+items.get(position).getMember_cnt());
        holder.grout_out.setOnClickListener(new ClickListener(holder,position));
        holder.layout.setOnClickListener(new ClickListener(holder,position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<GroupDTO> datas){
        int position=items.size();
        this.items.addAll(position,datas);
        notifyItemRangeChanged(position,datas.size());
    }

    class MyGroupHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView member_cnt;
        TextView grout_out;
        ImageView cover;
        ConstraintLayout layout;
        MyGroupHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            member_cnt=(TextView)itemView.findViewById(R.id.member_cnt);
            grout_out=(TextView)itemView.findViewById(R.id.group_out);
            cover=(ImageView)itemView.findViewById(R.id.cover);
            layout=(ConstraintLayout)itemView.findViewById(R.id.my_group_layout);
        }
    }
    class ClickListener implements View.OnClickListener{
        int position;
        MyGroupHolder holder;
        ClickListener(){}
        ClickListener(@NonNull MyGroupHolder holder, int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.my_group_layout:{
                    Intent intent =new Intent(mContext, PostActivity.class);
                    intent.putExtra("groupID",items.get(position).get_id());
                    mContext.startActivity(intent);
                    break;
                }
                case R.id.group_out:{
                    groupOutDialog(position);
                    break;
                }
            }
        }
    }
    public void groupOutDialog(Integer position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("그룹을 탈퇴하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                withdrawGroup(position);
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
    public void withdrawGroup(int position){
        RetrofitApi.getService().groupWithdraw(items.get(position).get_id()).enqueue(new retrofit2.Callback<RetrofitResponse>(){
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
}