package com.example.graduationproject.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {
    private static final String TAG = "로그인연동에러";
    private Context mContext;
    private Handler mHandler=new Handler();

    public void setContext(Context context){
        this.mContext=context;
    }
    @Override
    public void onSessionOpened() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d(TAG, "onFailure ");
                String message = "로그인 실패" + errorResult;

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    //에러로 인한 로그인 실패
                    toastMessage(message);
                } else {
                    //redirectMainActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed1 =" + errorResult);
                String message="세션 닫힘"+errorResult;
                toastMessage(message);
            }

            @Override
            public void onNotSignedUp() {
                Log.d(TAG, "onNotSignedUp ");
                String message="카카오 회원이 아닙니다.";
                toastMessage(message);
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                Log.d(TAG, "onSuccess ");
                Intent intent=new Intent(mContext, MainActivity.class);
                intent.putExtra("userID",userProfile.getId());
                intent.putExtra("userNicName",userProfile.getNickname());
                mContext.startActivity(intent);

            }
        });

    }
    // 세션 실패시
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.d(TAG, "onSessionOpenFailed ");
        String message="세션 오픈 실패";
        toastMessage(message);
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
