package com.example.graduationproject.community.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.CommunityVPadapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CommunityMainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private CommunityVPadapter adapter;
    private String userEmail;
    private Toolbar toolbar;
    private SearchView searchView;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchListener());
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
            if(tab.getPosition()==1){
                tvTitle.setText("친구");
            }else if(tab.getPosition()==2){
                tvTitle.setText("그룹");
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
    class SearchListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent=new Intent(CommunityMainActivity.this, SearchResultActivity.class);
            intent.putExtra("type","friend");
            intent.putExtra("query",query);
            startActivity(intent);
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}
