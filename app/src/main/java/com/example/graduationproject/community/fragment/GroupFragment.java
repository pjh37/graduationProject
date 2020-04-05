package com.example.graduationproject.community.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.GroupCreateActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GroupFragment extends Fragment {
    Button btnGroupCreate;
    Button btnCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_group,container,false);
        btnGroupCreate=(Button)rootView.findViewById(R.id.btnGroupCreate);
        btnCategory=(Button)rootView.findViewById(R.id.btnCategory);
        btnGroupCreate.setOnClickListener(new ClickListener());
        btnCategory.setOnClickListener(new ClickListener());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

                    break;
                }
            }
        }
    }
}
