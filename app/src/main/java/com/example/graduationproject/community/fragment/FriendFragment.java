package com.example.graduationproject.community.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.InviteAdapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    public static final int FRIENDSTATEINVITE=-1;
    public static final int FRIENDSTATEREJECT=0;
    public static final int FRIENDSTATEACCEPT=1;

    RecyclerView recyclerView;
    InviteAdapter adapter;
    TextView tvFriendCnt;
//    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_friend,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        tvFriendCnt=(TextView)rootView.findViewById(R.id.tvFriendCnt);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new InviteAdapter(getContext(),datas);
        recyclerView.setAdapter(adapter);
        getInviteList();
    }
    public void getInviteList(){
        //초대 요청 만 가져오기  state -> -1
        //거절 목록  state -> 0
        //수락목록 (진짜 친구목록)  state -> 1

        RetrofitApi.getService().getFriendList(Session.getUserEmail(),FRIENDSTATEINVITE).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    tvFriendCnt.setText(datas.size()+"명");
                    adapter.addItems(datas);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) { }
        });
    }
}
