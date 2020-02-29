package com.example.graduationproject.community.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.FriendAdapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    ImageView profileImage;
    RecyclerView recyclerView;
    FriendAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_setting,container,false);
        profileImage=(ImageView)rootView.findViewById(R.id.profile_image);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(getContext());
        adapter=new FriendAdapter(getContext(),datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this).load(R.drawable.custom_item)
                .into(profileImage);
        getFriendList();
    }
    public void getFriendList(){
        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());
        RetrofitApi.getService().friendSelect(input).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) { }
        });
    }
}
