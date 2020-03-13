package com.example.graduationproject.login;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.example.graduationproject.messageservice.MessagingService;

public class Session extends Application {
    private static String userEmail;
    private static String userName;
    private static String userImage;


    @Override
    public void onCreate() {
        super.onCreate();

    }
    public void setSession( String userEmail, String userName, String userImage){
        this.userEmail=userEmail;
        this.userName=userName;
        this.userImage=userImage;
    }
    public void messageServiceStart(){
        Intent intent=new Intent(this, MessagingService.class);
        intent.putExtra("userEmail",userEmail);
        intent.putExtra("userName",userName);
        intent.putExtra("userImage",userImage);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            this.startForegroundService(intent);
        }else{
            this.startService(intent);
        }
    }
    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        Session.userEmail = userEmail;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Session.userName = userName;
    }

    public static void setUserImage(String userImage) {
        Session.userImage = userImage;
    }

    public static String getUserImage() {
        return userImage;
    }
    public static String getTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public  void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
