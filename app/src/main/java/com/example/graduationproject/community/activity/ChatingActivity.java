package com.example.graduationproject.community.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.graduationproject.R;

public class ChatingActivity extends AppCompatActivity {
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chating);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new ClickListener());
    }
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSend:{

                    break;
                }
            }
        }
    }
}
