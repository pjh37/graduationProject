package com.example.graduationproject.community.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{
private ArrayList<FriendDTO> items=new ArrayList<>();
private Context mContext;
private View itemView;
public SearchAdapter(Context context, ArrayList<FriendDTO> items){
        this.mContext=context;
        this.items=items;
        }
@NonNull
@Override
public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_search_item,parent,false);
        return new SearchHolder(itemView);
}

@Override
public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        if(items.get(position).getProfileImageUrl()==null){
        Glide.with(mContext)
        .load(R.drawable.profile)
        .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
        Glide.with(mContext)
        .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getProfileImageUrl()+".jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }
        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.btnFriendRequest.setOnClickListener(new ClickListener(items.get(position).getUserEmail()));
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
class SearchHolder extends RecyclerView.ViewHolder{
    TextView userEmail;
    ImageView profileImage;
    Button btnFriendRequest;

    SearchHolder(View itemView){
        super(itemView);
        profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
        userEmail=(TextView)itemView.findViewById(R.id.userEmail);
        btnFriendRequest=(Button)itemView.findViewById(R.id.btnFriendRequest);
    }
}
class ClickListener implements View.OnClickListener{
    private String to;
    public ClickListener(String to){
        this.to=to;
    }
    @Override
    public void onClick(View view) {
        HashMap<String, Object> input = new HashMap<>();
        input.put("sender", Session.getUserEmail());
        input.put("receiver",to);
        input.put("state",-1);
        input.put("time",Session.getTime());
        RetrofitApi.getService().friendRequest(input).enqueue(new retrofit2.Callback<Boolean>(){
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) { }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });
    }

}
}