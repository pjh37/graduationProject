package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupHolder>{

    private ArrayList<GroupDTO> items;
    private Context mContext;
    private View itemView;

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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(30)))
                .into(holder.cover);
        holder.title.setText(items.get(position).getTitle().substring(1,items.get(position).getTitle().length()-1));
        holder.member_cnt.setText("멤버 : "+items.get(position).getMember_cnt());
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
        ImageView cover;
        ConstraintLayout layout;
        MyGroupHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            member_cnt=(TextView)itemView.findViewById(R.id.member_cnt);
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
            }
        }
    }
}