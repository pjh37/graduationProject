package com.example.graduationproject.login;

import android.content.Context;
import android.util.Log;

import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class LogoutCallback extends LogoutResponseCallback {
    private Context mContext;
    public void setContext(Context context){
        this.mContext=context;
    }
    @Override
    public void onCompleteLogout() {
        Log.v("로그아웃","로그아웃 성공");
    }
}
