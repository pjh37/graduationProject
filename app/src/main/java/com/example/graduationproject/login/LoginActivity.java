package com.example.graduationproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.community.activity.CommunityMainActivity;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "로그인연동에러";//에러나면 하드코딩해서 이메일 넘길것!!!
    private static final int RC_SIGN_IN=1;
    private Session session;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    //LoginButton btnKaKaoLogin;
    CheckBox chkAutoLogin;
    private Button btnGoSurvey;
    private Button btnGoCommunity;
    private SignInButton btnLogin;
    private Button btnExit;
    private LinearLayout autoLogin;
    boolean fileReadPermission;
    boolean fileWritePermission;
    boolean internetPermission;
    //Use for Widget Login
    private SharedPreferences widgetLoginPref;
    private SharedPreferences.Editor widgetLoginEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });

        checkPermission();
        session =(Session)getApplication();
        btnGoSurvey=(Button)findViewById(R.id.btnGoSurvey);
        btnGoCommunity=(Button)findViewById(R.id.btnGoCommunity);
        btnLogin=(SignInButton)findViewById(R.id.sign_in_button);

        btnExit=(Button)findViewById(R.id.btn_exit);
//        btnExit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_exit, 0, 0, 0);

        autoLogin=(LinearLayout)findViewById(R.id.autoLogin);
        btnGoSurvey.setVisibility(View.GONE);
        btnGoCommunity.setVisibility(View.GONE);
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
        widgetLoginPref=getSharedPreferences("widget_LoginOnApp", MODE_PRIVATE);
        editor=mPref.edit();
        widgetLoginEdit=widgetLoginPref.edit();

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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.FOREGROUND_SERVICE)==PackageManager.PERMISSION_GRANTED){

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
            case R.id.btnGoSurvey:
                goToSurvey();
                break;
            case R.id.btnGoCommunity:
                goToCommunity();
                break;
//            case R.id.btn_exit:
//                goToExit();
//                break;
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
            GoogleSignInAccount account=completeTask.getResult(ApiException.class); // @.@
            firebaseAuthWithGoogle(account);
            String email=account.getEmail();
            Log.v(TAG,email);
        }catch (Exception e){
            e.printStackTrace();
        }
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

                            session.setSession(user.getEmail(),user.getDisplayName(),String.valueOf(user.getPhotoUrl()),user.getPhotoUrl());
                            saveLoginInfo(user.getEmail(),user.getDisplayName(),user.getPhotoUrl());
                            session.messageServiceStart();
                            loginSuccess();

                            //위젯 로그인용
                            widgetLoginEdit.putString("LoginID",user.getEmail());
                            widgetLoginEdit.putBoolean("IsUserLogin",true);
                            widgetLoginEdit.apply();


                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void loginSuccess(){

        btnGoSurvey.setVisibility(View.VISIBLE);
        btnGoCommunity.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);
        autoLogin.setVisibility(View.GONE);
        RetrofitApi.getService().userRegister(Session.getUserEmail()).enqueue(new retrofit2.Callback<Boolean>(){
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()!=null){
                    Log.v("테스트","유저 저장 완료");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });

    }

    private void goToSurvey(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class); // new
        startActivity(intent);
    }
    private void goToCommunity(){
        Intent intent=new Intent(LoginActivity.this, CommunityMainActivity.class); // new
        startActivity(intent);
    }



    private void saveLoginInfo(String userEmail,String userName,Uri userImage){
        SharedPreferences login_info=getSharedPreferences("loginConfig",0);
        SharedPreferences.Editor editor=login_info.edit();
        editor.putString("userEmail",userEmail);
        editor.putString("userName",userName);
        editor.putString("userImage",String.valueOf(userImage));
        editor.commit();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("저장").setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 로그인 안하고 종료
//    private void goToExit() {
//        moveTaskToBack(true);
//        finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
}
