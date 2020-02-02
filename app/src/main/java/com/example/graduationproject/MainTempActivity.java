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
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.form.FormActivity;
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
    RecyclerView.Adapter uploadedSurveyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UploadedSurveyDTO> datas;

    private String url;
    private ProgressBar progressBar;
    private boolean isFinish;

    public String userEmail;
    public String userName;
    public Uri userImage;

    private ViewPager viewPager;
    private MainVPAdapter mainVPAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_temp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);

        url=getString(R.string.baseUrl);
        isFinish=false;
        datas=new ArrayList<>();

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userName = intent.getStringExtra("userName");
        userImage = intent.getExtras().getParcelable("userImage");

        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        View NavHeader = navigationView.getHeaderView(0); // LinearLayout
        TextView txtUserID = NavHeader.findViewById(R.id.txtUserID);
        txtUserID.setText(userName);
        ImageView imvUserImg = NavHeader.findViewById(R.id.imvUserImg);
        Glide.with(this).load(userImage).into(imvUserImg);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.offline: {
                        Intent intent = new Intent(getApplicationContext(), OfflineFormActivity.class);
                        intent.putExtra("userEmail", userEmail);
                        startActivity(intent);
                        break;
                    }
                    case R.id.template: {
                        Toast.makeText(getApplicationContext(), "template", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.notification: {
                        Toast.makeText(getApplicationContext(), "notification 미완성", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.help: {
                        Toast.makeText(getApplicationContext(), "help 미완성", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.share: {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.baseUrl) + "survey/6");
                        Intent chooser = Intent.createChooser(intent, "공유");
                        startActivity(chooser);
                        break;
                    }
                }
                return true;
            }
        });
        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager(), args);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_menu:
                Intent intent = new Intent(this, FormActivity.class);
                intent.putExtra("userEmail", userEmail);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                break;
        }
    }
}
