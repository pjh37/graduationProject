package com.example.graduationproject.login;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

public class Session extends Application {
    private static String userEmail;
    private static String userName;
    private static Uri userImage;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void setSession(String userEmail, String userName, Uri userImage){
        this.userEmail=userEmail;
        this.userName=userName;
        this.userImage=userImage;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getUserName() {
        return userName;
    }

    public static Uri getUserImage() {
        return userImage;
    }
    public static String getTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public  void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
