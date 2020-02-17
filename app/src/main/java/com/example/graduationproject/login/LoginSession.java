package com.example.graduationproject.login;

import android.app.Application;
import android.net.Uri;

public class LoginSession extends Application {
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
}
