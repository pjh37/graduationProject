package com.example.graduationproject.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.CommunityVPadapter;
import com.google.android.material.tabs.TabLayout;

public class CommunityMainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private CommunityVPadapter adapter;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        adapter=new CommunityVPadapter(getSupportFragmentManager(),args);
        viewPager.setAdapter(adapter);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabSelectListener());


    }
    class TabSelectListener implements TabLayout.OnTabSelectedListener{
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
