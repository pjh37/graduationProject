package com.example.graduationproject.community.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.CommunityVPadapter;
import com.example.graduationproject.community.fragment.HomeFragment;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.graduationproject.community.fragment.HomeFragment.userSensorListner;

public class CommunityMainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchListener());
        SearchView.SearchAutoComplete searchAutoComplete=(SearchView.SearchAutoComplete)findViewById(R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigatorListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

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
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
    class NavigatorListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            menuItem.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()){
                case R.id.mailbox:{

                    break;
                }
                case R.id.chatroom:{
                    Intent intent=new Intent(CommunityMainActivity.this,ChatServiceActivity.class);
                    startActivity(intent);
                    break;
                }
            }
            return false;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(HomeFragment.mSensorManager != null && userSensorListner != null)
            HomeFragment.mSensorManager.unregisterListener(userSensorListner);
    }
}
