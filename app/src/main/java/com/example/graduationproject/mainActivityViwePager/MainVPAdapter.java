package com.example.graduationproject.mainActivityViwePager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainVPAdapter extends FragmentStatePagerAdapter {
    private MainVPMySurveyFragment mainVPMySurveyFragment;
    private MainVPSurveyFragment mainVPSurveyFragment;

    public MainVPAdapter(FragmentManager fm, Bundle args){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mainVPMySurveyFragment=new MainVPMySurveyFragment();
        this.mainVPMySurveyFragment.setArguments(args);
        this.mainVPSurveyFragment=new MainVPSurveyFragment();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mainVPMySurveyFragment;
            case 1:
                return mainVPSurveyFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
