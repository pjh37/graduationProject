package com.example.graduationproject.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SessionCallback implements ISessionCallback {
    private static final String TAG = "로그인연동에러";
    private Context mContext;
    private Handler mHandler=new Handler();
    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("properties.profile_image");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(MeV2Response response) {
                Logger.d("user id : " + response.getId());
                Logger.d("email: " + response.getKakaoAccount().getEmail());
                Log.d(TAG, "onSuccess "+response.getNickname());
                Intent intent=new Intent(mContext, MainActivity.class);
                intent.putExtra("userEmail",response.getId());
                intent.putExtra("userNicName",response.getNickname());
                mContext.startActivity(intent);
            }


        });
    }
    public void setContext(Context context){
        this.mContext=context;
    }
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    // 세션 실패시
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.d(TAG, "onSessionOpenFailed ");
        String message="세션 오픈 실패";
        //toastMessage(message);
    }
    public void toastMessage(final String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
