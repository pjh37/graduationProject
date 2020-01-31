package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.mainActivityViwePager.MainVPAdapter;
import com.example.graduationproject.offlineform.OfflineFormActivity;
import com.example.graduationproject.result.ResultViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainTempActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    RecyclerView uploadedSurveyRV;
    RecyclerView.Adapter  uploadedSurveyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ViewPager viewPager;
    private ArrayList<UploadedSurveyDTO> datas;
    public  String userEmail;
    private String url;
    private ProgressBar progressBar;
    private boolean isFinish;
    private MainVPAdapter mainVPAdapter;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_temp);
        url=getString(R.string.baseUrl);
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");
        drawerLayout=(DrawerLayout)findViewById(R.id.main_drawer);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        mTabLayout=(TabLayout)findViewById(R.id.tabs);
        isFinish=false;
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                TextView txtUserID=(TextView)findViewById(R.id.txtUserID);
                txtUserID.setText(userEmail);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()){
                    case R.id.offline: {
                        Intent intent = new Intent(getApplicationContext(), OfflineFormActivity.class);
                        intent.putExtra("userEmail",userEmail);
                        startActivity(intent);
                        break;
                    }
                    case R.id.template: {
                        Toast.makeText(getApplicationContext(), "template", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.notification: {

                    }
                    case R.id.help:{

                    }
                    case R.id.share: {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.baseUrl)+"survey/6");
                        Intent chooser=Intent.createChooser(intent,"공유");
                        startActivity(chooser);
                        break;
                    }
                }
                return true;
            }
        });
        mainVPAdapter=new MainVPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainVPAdapter);
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
}
