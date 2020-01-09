package com.example.graduationproject.result;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class ResultViewPagerAdapter extends FragmentStatePagerAdapter {
    private SummaryViewFragment summaryViewFragment;
    private IndividualViewFragment individualViewFragment;
    public ResultViewPagerAdapter(FragmentManager fm, Bundle args){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
       this.summaryViewFragment=new SummaryViewFragment();
       this.individualViewFragment=new IndividualViewFragment();
        individualViewFragment.setArguments(args);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return summaryViewFragment;
            case 1:
                return individualViewFragment;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
