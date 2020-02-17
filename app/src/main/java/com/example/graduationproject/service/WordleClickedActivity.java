package com.example.graduationproject.service;

// WordCloud 에 출력된 키워드를 클릭 시 WordleClickedActivity 가 실행되도록 합니다.
// 제목에 키워드가 포함된 설문조사의 목록을 출력하도록 합니다.
// 설문조사를 클릭 시 설문 결과를 표시하도록 합니다.

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
