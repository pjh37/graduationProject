package com.example.graduationproject.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.graduationproject.R;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "로그인연동에러";
    private SessionCallback callback;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    LoginButton btnKaKaoLogin;
    CheckBox chkAutoLogin;
    boolean fileReadPermission;
    boolean fileWritePermission;
    boolean internetPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        btnKaKaoLogin=(LoginButton)findViewById(R.id.com_kakao_login);
        chkAutoLogin=(CheckBox)findViewById(R.id.chkAutoLogin);
        mPref=getSharedPreferences("login",MODE_PRIVATE);
        editor=mPref.edit();
        getHashKey();
        callback=new SessionCallback();
        callback.setContext(this);
        Session.getCurrentSession().addCallback(callback);
        checkPermission();
        if(mPref.getBoolean("isAutoLogin",false)){
            Session.getCurrentSession().open(AuthType.KAKAO_TALK,this);
        }
    }
    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            fileReadPermission=true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            fileWritePermission=true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED){
            internetPermission=true;
        }
        if(!fileReadPermission||!fileWritePermission||!internetPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET},100);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        if(mPref.getBoolean("isAutoLogin",false)){
            chkAutoLogin.setChecked(true);
        }
    }
    private void getHashKey(){
        try {                                                        // 패키지이름을 입력해줍니다.
            PackageInfo info = getPackageManager().getPackageInfo("com.example.graduationproject", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG,"key_hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.chkAutoLogin:
                if(chkAutoLogin.isChecked()){
                    editor.putBoolean("isAutoLogin",true);
                    editor.apply();
                }else{
                    editor.putBoolean("isAutoLogin",false);
                    editor.apply();
                }
                break;
        }
    }

}
