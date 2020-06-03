package com.example.graduationproject.community.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.GroupCreateActivity;
import com.example.graduationproject.community.activity.MyGroupActivity;
import com.example.graduationproject.community.adapter.FriendAdapter;
import com.example.graduationproject.community.adapter.GroupAdapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupFragment extends Fragment {
    private final static Integer COUNT=20;
    Integer offset=0;

    Button btnGroupCreate;
    Button btnCategory;
    Button btnMyGroup;
    //TextView tvCategory;// for later

    RecyclerView recyclerView;
    GroupAdapter adapter;


    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupDTO> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_group,container,false);
        btnGroupCreate=(Button)rootView.findViewById(R.id.btnGroupCreate);
        btnCategory=(Button)rootView.findViewById(R.id.btnCategory);
        btnMyGroup=(Button)rootView.findViewById(R.id.btnMyGroup);
        btnGroupCreate.setOnClickListener(new ClickListener());
        btnCategory.setOnClickListener(new ClickListener());
        btnMyGroup.setOnClickListener(new ClickListener());

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(getContext());
        adapter=new GroupAdapter(getContext(),datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        findAllGroup();
//    }

    public void findAllGroup(){
        RetrofitApi.getService().grouptGet(Session.getUserEmail(),COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<GroupDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<GroupDTO>> call, Response<ArrayList<GroupDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<GroupDTO>> call, Throwable t) { }
        });
    }

    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGroupCreate:{
                    Intent intent=new Intent(getContext(), GroupCreateActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnCategory:{
                    Toast.makeText(getContext(),"btnCategory",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.btnMyGroup:{
                    Intent intent=new Intent(getContext(), MyGroupActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        datas.clear();
        adapter.datasClear();
        findAllGroup();
//        Log.d("mawang","GroupFragment onResume - 짠");
    }

}
