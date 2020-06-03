package com.example.graduationproject.login;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.example.graduationproject.messageservice.MessagingService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Session extends Application {
    private static String userEmail;
    private static String userName;
    private static String userImage;



    private static Uri userImage_uri;


    @Override
    public void onCreate() {
        super.onCreate();

    }
    public void setSession( String userEmail, String userName, String userImage,Uri userImage_uri){
        this.userEmail=userEmail;
        this.userName=userName;
        this.userImage=userImage;
        this.userImage_uri=userImage_uri;
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

    public static Uri getUserImage_uri() {return userImage_uri;}
    public static void setUserImage_uri(Uri userImage_uri) {Session.userImage_uri = userImage_uri;}

    public static String getTime(){
        long now=Long.valueOf(String.valueOf(System.currentTimeMillis()));
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }


    public  void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
