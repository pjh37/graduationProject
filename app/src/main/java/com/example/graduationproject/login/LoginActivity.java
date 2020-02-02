package com.example.graduationproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.MainTempActivity;
import com.example.graduationproject.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "로그인연동에러";//에러나면 하드코딩해서 이메일 넘길것!!!
    private static final int RC_SIGN_IN=1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
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
        checkPermission();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        firebaseAuth=FirebaseAuth.getInstance();
        SignInButton signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener((view)->{
            onClick(view);
        });
        chkAutoLogin=(CheckBox)findViewById(R.id.chkAutoLogin);
        mPref=getSharedPreferences("login",MODE_PRIVATE);
        editor=mPref.edit();


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
            case R.id.sign_in_button:
               signIn();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
    }
    private void signIn(){
        Intent intent= mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completeTask){
        try{
            GoogleSignInAccount account=completeTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            String email=account.getEmail();
            Log.v(TAG,email);
        }catch (Exception e){}
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this, MainTempActivity.class); // new
                            intent.putExtra("userEmail",user.getEmail());
                            intent.putExtra("userName",user.getDisplayName());
                            intent.putExtra("userImage",user.getPhotoUrl());
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
