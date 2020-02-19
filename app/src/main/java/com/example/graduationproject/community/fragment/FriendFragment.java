package com.example.graduationproject.community.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.R;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.LoginSession;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Response;

public class FriendFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_friend,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public void getInviteList(){
        //초대 요청 만 가져오기  state -> -1
        //거절 목록  state -> 0
        //수락목록 (진짜 친구목록)  state -> 1
        RetrofitApi.getService().getFriendList(LoginSession.getUserEmail(),-1).enqueue(new retrofit2.Callback<FriendDTO>(){
            @Override
            public void onResponse(Call<FriendDTO> call, Response<FriendDTO> response) {

            }

            @Override
            public void onFailure(Call<FriendDTO> call, Throwable t) {

            }
        });
    }
}
