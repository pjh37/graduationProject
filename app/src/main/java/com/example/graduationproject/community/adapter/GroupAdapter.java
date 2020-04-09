package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder>{
    private static final int PUBLIC=0;
    private static final int PRIVATE=1;
    private ArrayList<GroupDTO> items;
    private Context mContext;
    private View itemView;

    public GroupAdapter(Context context, ArrayList<GroupDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.communtiy_fragment_group_item,parent,false);
        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"group/image/cover/"+items.get(position).get_id())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(20)))
                .into(holder.cover);
        holder.title.setText(items.get(position).getTitle().substring(1,items.get(position).getTitle().length()-1));
        holder.member_cnt.setText("멤버 : "+items.get(position).getMember_cnt());
        holder.btnJoin.setOnClickListener(new ClickListener(holder,position));
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

    class GroupHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView member_cnt;
        ImageView cover;
        Button btnJoin;
        GroupHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            member_cnt=(TextView)itemView.findViewById(R.id.member_cnt);
            cover=(ImageView)itemView.findViewById(R.id.cover);
            btnJoin=(Button)itemView.findViewById(R.id.btnJoin);
        }
    }
    class ClickListener implements View.OnClickListener{
        int position;
        GroupHolder holder;

        ClickListener(@NonNull GroupHolder holder,int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View view) {
            if(holder.btnJoin.getText().equals("가입")){
                holder.btnJoin.setText("탈퇴");
                //공개, 비공개 그룹인지 확인
                //비공개일 경우 비밀번호를 입력하는 다이얼로그 창 열기
                groupJoin(items.get(position).get_id());
                if(items.get(position).getAuthority()==PRIVATE){

                }
            }else{
                holder.btnJoin.setText("가입");
            }
        }
    }
    public void groupJoin(Integer groupID){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userEmail", Session.getUserEmail());
        hashMap.put("group_id",groupID);
        RetrofitApi.getService().groupJoin(hashMap).enqueue(new retrofit2.Callback<Boolean>(){
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) { }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });
    }
}
