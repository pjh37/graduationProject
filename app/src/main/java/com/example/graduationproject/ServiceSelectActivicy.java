package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.service.ServiceTabActivity;

public class ServiceSelectActivicy extends AppCompatActivity {

    Intent login_intent;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_select_activicy);
        login_intent = getIntent();
        userEmail = login_intent.getStringExtra("userEmail");

        Toast.makeText(getApplicationContext(),userEmail,Toast.LENGTH_LONG).show();
    }

    public void serviceSelectClick(View v){
        switch (v.getId()){
            case R.id.serviceSelect_Survey:{
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
                break;
            }
            case R.id.serviceSelect_Service:{
                Intent intent = new Intent(getApplicationContext(), ServiceTabActivity.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
                break;
            }
        }
    }
}
