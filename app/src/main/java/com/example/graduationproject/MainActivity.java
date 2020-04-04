package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.form.FormActivity;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.mainActivityViwePager.MainVPAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

//    RecyclerView uploadedSurveyRV;
//    RecyclerView.Adapter uploadedSurveyAdapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private ArrayList<UploadedSurveyDTO> datas;
//    private String url;
//    private ProgressBar progressBar;
//    private boolean isFinish;

    private Session session;

    private static String userEmail;
    private static String userName;
    private static Uri userImage;

    private ViewPager viewPager;
    private MainVPAdapter mainVPAdapter;
    private TabLayout mTabLayout;

    public static final int categoryNumber=110;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session=(Session)getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);

//        url=getString(R.string.baseUrl);
//        isFinish=false;
//        datas=new ArrayList<>();

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
//        progressBar=(ProgressBar)findViewById(R.id.progress);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userName = intent.getStringExtra("userName");
        userImage = intent.getExtras().getParcelable("userImage");
//        session.setSession(userEmail,userName,userImage);
        //session.messageServiceStart();



        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        View NavHeader = navigationView.getHeaderView(0); // LinearLayout
        TextView txtUserID = NavHeader.findViewById(R.id.txtUserID);
        txtUserID.setText(userName);
        ImageView imvUserImg = NavHeader.findViewById(R.id.imvUserImg);
        Glide.with(this).load(userImage).into(imvUserImg);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true); //false
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.offline: {
                        //                        Intent intent = new Intent(getApplicationContext(), old_OfflineFormActivity.class);
//                        intent.putExtra("userEmail", userEmail);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "offline closed", Toast.LENGTH_SHORT).show();
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
                        //                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_SEND);
//                        intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.baseUrl) + "survey/6");
//                        Intent chooser = Intent.createChooser(intent, "공유");
//                        startActivity(chooser);

                        Toast.makeText(getApplicationContext(), "share 뭘 공유해", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });
        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager());
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

    // Session class 로 이용해도 된다.
    public static Uri getUserImage() {return userImage;}
    public static String getUserName() {return userName;}
    public static String getUserEmail() {return userEmail;}
}
