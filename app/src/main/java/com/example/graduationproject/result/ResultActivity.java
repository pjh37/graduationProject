package com.example.graduationproject.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.graduationproject.R;
import com.example.graduationproject.login.Session;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private ResultViewPagerAdapter pagerAdapter;
    private int form_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent=getIntent();
//        userEmail=intent.getStringExtra("userEmail");
        form_id=intent.getIntExtra("form_id",-1);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Result");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        url=getString(R.string.baseUrl);//getString(R.string.baseUrl);//http://192.168.35.42:8001
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        mTabLayout=(TabLayout)findViewById(R.id.tabs);

        Bundle args=new Bundle();
//        args.putString("userEmail", Session.getUserEmail());
        args.putInt("form_id",form_id);

        pagerAdapter=new ResultViewPagerAdapter(getSupportFragmentManager(),args);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
