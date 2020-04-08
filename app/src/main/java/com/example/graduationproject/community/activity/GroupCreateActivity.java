package com.example.graduationproject.community.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;

public class GroupCreateActivity extends AppCompatActivity{
    private static final int PICK_FROM_ALBUM=1;
    private Spinner spinner;
    private EditText title;
    private EditText description;
    private RadioGroup radioGroup;
    private Button btnGroupCreate;
    private ImageView coverImage;
    LinearLayout addCoverImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addCoverImage=(LinearLayout)findViewById(R.id.addCoverImage);
        addCoverImage.setOnClickListener(new ClickListener());
        spinner=(Spinner)findViewById(R.id.categoryMenu);
        title=(EditText)findViewById(R.id.editGroupTitle);
        description=(EditText)findViewById(R.id.editGroupDescription);
        radioGroup=(RadioGroup)findViewById(R.id.btnRadioGroup);
        coverImage=(ImageView)findViewById(R.id.coverImage);
    }
    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.addCoverImage:{
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            Uri fileUri = data.getData();
            Glide.with(GroupCreateActivity.this).load(fileUri).into(coverImage);
            //profileChange(getRealPathFromURI(fileUri));
        }
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
