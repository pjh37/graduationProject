package com.example.graduationproject.community.adapter;

import android.os.Bundle;

import com.example.graduationproject.community.fragment.FriendFragment;
import com.example.graduationproject.community.fragment.GroupFragment;
import com.example.graduationproject.community.fragment.HomeFragment;
import com.example.graduationproject.community.fragment.SettingFragment;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CommunityVPadapter extends FragmentStatePagerAdapter {
    private HomeFragment homeFragment;
    private FriendFragment friendFragment;
    private GroupFragment groupFragment;
    private SettingFragment settingFragment;

    // no need
//    public CommunityVPadapter(FragmentManager fm, Bundle args){
//        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        homeFragment=new HomeFragment();
//        friendFragment=new FriendFragment();
//        groupFragment=new GroupFragment();
//        settingFragment=new SettingFragment();
//    }
    public CommunityVPadapter(FragmentManager fm){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        homeFragment=new HomeFragment();
        friendFragment=new FriendFragment();
        groupFragment=new GroupFragment();
        settingFragment=new SettingFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return homeFragment;
            case 1:
                return friendFragment;
            case 2:
                return groupFragment;
            case 3:
                return settingFragment;
            default:
                return null;
        }

    }
    @Override
    public int getCount() {
        return 4;
    }
}
