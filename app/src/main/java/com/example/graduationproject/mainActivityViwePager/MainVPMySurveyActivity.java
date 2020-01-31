package com.example.graduationproject.mainActivityViwePager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.R;
import com.example.graduationproject.form.FormSaveManager;
import com.example.graduationproject.offlineform.FormItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MainVPMySurveyActivity extends Fragment {
    RecyclerView offlineForm;
    RecyclerView.Adapter  offlineFormAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FormItem> formItem;
    private FormSaveManager formSaveManager;
    public String userEmail;
    public MainVPMySurveyActivity(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_main_vp_mysurvey,container,false);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
