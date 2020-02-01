package com.example.graduationproject.mainActivityViwePager;

import android.os.Bundle;

import com.example.graduationproject.result.IndividualViewFragment;
import com.example.graduationproject.result.SummaryViewFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainVPAdapter extends FragmentStatePagerAdapter {
    private MainVPMySurveyActivity mainVPMySurveyActivity;
    private MainVPSurveyActivity mainVPSurveyActivity;

    public MainVPAdapter(FragmentManager fm){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mainVPMySurveyActivity=new MainVPMySurveyActivity();
        this.mainVPSurveyActivity=new MainVPSurveyActivity();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mainVPMySurveyActivity;
            case 1:
                return mainVPSurveyActivity;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
