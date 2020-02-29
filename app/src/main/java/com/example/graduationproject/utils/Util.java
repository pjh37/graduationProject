package com.example.graduationproject.utils;

import android.app.Application;
import android.widget.Toast;

public class Util extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public String getTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
