package com.example.graduationproject.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.graduationproject.R;

public class WordleClickedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordle_clicked);
        Intent intent = getIntent();

        String title = intent.getStringExtra("Keyword");
        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.wordleClicked_toolbar);
        tb.setTitle(title);
    }
}
