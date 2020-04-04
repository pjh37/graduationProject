package com.example.graduationproject.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
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

import java.util.ArrayList;

import static com.example.graduationproject.widget.HomeSurveyWidget.ACTION_CONFIGURE_FINISHED;

public class WidgetConfigActivity extends Activity {

    int mAppWidgetId;
    AppWidgetManager appWidgetManager;
    RemoteViews remoteViews;

    public static final String SET_EXPECT_RESPONSE = ".widget.WidgetConfigActivity.SET_EXPECT_RESPONSE";
    private static final String TAG = "로그인연동에러";
    private static final int RC_SIGN_IN=1;
    private BroadcastReceiver setExpectBR;
    private Session loginSession;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private ArrayList<UploadedSurveyDTO> data = new ArrayList<>();
    private WidgetListRV widgetListRV;
    private RecyclerView recyclerView;
    private TextView waitText;

    // 임시, 추후 클래스 넘겨받는 걸로 수정할 것.
    private int rvPosition;
    private int expectValue;
    private boolean isReceived = false;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        appWidgetManager = AppWidgetManager.getInstance(this);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.home_survey_widget);

        preferences = getSharedPreferences("testPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SET_EXPECT_RESPONSE);
        setExpectBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(!data.isEmpty()){
                    expectValue = intent.getIntExtra("expectValue", 0);
                    rvPosition = intent.getIntExtra("position", 0);
                    editor.putInt("_id", data.get(rvPosition).get_id());
                    editor.putInt("expectValue", expectValue);
                    editor.putString("title", data.get(rvPosition).getTitle());
                    editor.putInt("response", data.get(rvPosition).getResponse_cnt());
                    editor.putString("time",data.get(rvPosition).getTime());
                    editor.apply();

                    isReceived = true;

                }
            }
        };
        registerReceiver(setExpectBR, intentFilter);

        waitText = (TextView)findViewById(R.id.widget_pleaseLogin);
        widgetListRV = new WidgetListRV(this, data);
        recyclerView = (RecyclerView)findViewById(R.id.widget_surveyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        recyclerView.setAdapter(widgetListRV);

        loginSession=(Session)getApplication();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        firebaseAuth=FirebaseAuth.getInstance();

        SignInButton signInButton=findViewById(R.id.widget_signIn);
        signInButton.setOnClickListener((view)->{
            Intent intent= mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent,RC_SIGN_IN);

        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            mAppWidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isReceived){}
                appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

                Intent serviceIntent = new Intent(getApplicationContext(), WidgetUpdateService.class);
                ComponentName componentName = new ComponentName(getApplicationContext(), HomeSurveyWidget.class);
                int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
                serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
                getApplicationContext().startService(serviceIntent);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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

                            loginSession.setSession(user.getEmail(),user.getDisplayName(),String.valueOf(user.getPhotoUrl()));

                            waitText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            data.clear();
                            getSurveyList(user.getEmail());

                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void getSurveyList(String userEmail){
        RetrofitApi.getService().getSurveyList(userEmail).enqueue(new retrofit2.Callback<ArrayList<UploadedSurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, retrofit2.Response<ArrayList<UploadedSurveyDTO>> response) {
                widgetListRV.addItem(response.body());
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, Throwable t) { }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(setExpectBR);
    }

}
