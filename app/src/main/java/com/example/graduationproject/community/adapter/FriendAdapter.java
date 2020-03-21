package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.FriendDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{
    private ArrayList<FriendDTO> items;
    private Context mContext;
    private int type;
    private static final int FRIEND_LIST=0;//단순 친구목록 , 옆에 친구 삭제 버튼이 함께한다
    private static final int INVITE_LIST=1;//친구 삭제버튼이 사라지고 채팅방초대 체크박스가 활성화 된다
    private View itemView;
    public FriendAdapter(Context context, ArrayList<FriendDTO> items,int type){
        this.mContext=context;
        this.items=items;
        this.type=type;
    }
    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_item,parent,false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        if(items.get(position).getProfileImageUrl()==null){
            Glide.with(mContext)
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(mContext)
                    .load(mContext.getString(R.string.baseUrl)+"user/profile/"+items.get(position).getProfileImageUrl()+".jpg")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }

        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        if(type==FRIEND_LIST){
            holder.chkInviteChatRoom.setVisibility(View.GONE);
        }else if(type==INVITE_LIST){
            holder.btnFriendDelete.setVisibility(View.GONE);
            holder.chkInviteChatRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.get(position).setSelected(holder.chkInviteChatRoom.isChecked());
                }
            });
        }
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
    public JSONArray getCheckedFriends(){
        ArrayList<String> friends=new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<items.size();i++){
            if(items.get(i).isSelected()){
                //friends.add(items.get(i).getUserEmail());
                jsonArray.put(items.get(i).getUserEmail());
            }
        }
        //임시 테스트용
        jsonArray.put("jjjj1352@naver.com");
        return jsonArray;
    }
    class FriendHolder extends RecyclerView.ViewHolder{
        TextView userEmail;
        ImageView profileImage;
        Button btnFriendDelete;
        CheckBox chkInviteChatRoom;
        FriendHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
            userEmail=(TextView)itemView.findViewById(R.id.userEmail);
            btnFriendDelete=(Button)itemView.findViewById(R.id.btnFriendDelete);
            chkInviteChatRoom=(CheckBox)itemView.findViewById(R.id.chkInviteChatRoom);
        }
    }
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }
}
