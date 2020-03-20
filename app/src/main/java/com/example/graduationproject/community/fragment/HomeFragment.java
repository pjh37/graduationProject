package com.example.graduationproject.community.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.example.graduationproject.service.KoreanPhraseExtractorApi;
import com.example.graduationproject.service.OnClickWithOnTouchListener;
import com.example.graduationproject.service.WordleClickedActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // WordCloud
    private String[] wordCloud;
    private ArrayList<SurveyDTO> datas = new ArrayList<>();
    private ArrayList<String> extractedTitles = new ArrayList<>();
    private KoreanPhraseExtractorApi koreanPhraseExtractor;
    private int pageCount = 1;
    private WebView d3;
    private boolean isFinish = false;
    private boolean isWordReady = false;
    private ProgressBar progressBar;
    private View root;

    // 가속도, 자이로센서
    // CommunityMainActivity 파괴시 해제
    public static SensorManager mSensorManager = null;
    public static UserSensorListner userSensorListner = null;
    private Sensor mGyroscopeSensor = null;
    private Sensor mAccelerometer = null;

    private float[] mGyroValues = new float[3];
    private float[] mAccValues = new float[3];
    private double mAccPitch, mAccRoll;

    // 상보필터에서 사용
    private float a = 0.2f;
    private static final float NS2S = 1.0f/1000000000.0f;
    private double pitch = 0, roll = 0;
    private double timestamp;
    private double dt;
    private double temp;
    private boolean running;
    private boolean gyroRunning;
    private boolean accRunning;

    // webview - android 통신브릿지
    private class AndroidBridge{
        final public Handler handler = new Handler();

        @JavascriptInterface
        public void getClickedWord(final String arg){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),arg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), WordleClickedActivity.class);
                    intent.putExtra("Keyword",arg);
                    startActivity(intent);

                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_wordle, container, false);

        // 가속도센서,자이로센서
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        userSensorListner = new UserSensorListner();
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        checkGyroAcc();

        progressBar = (ProgressBar)root.findViewById(R.id.wordle_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getSurveyTitles();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
                if(isFinish){
                    extractedTitles = extractTitleFromSurveyDTO(datas);

                    koreanPhraseExtractor = new KoreanPhraseExtractorApi(extractedTitles);
                    ArrayList<String> str = koreanPhraseExtractor.extractPhrase();

                    wordCloud = new String[str.size()];
                    int i = 0;
                    for(String string : str){
                        wordCloud[i++] = string;
                    }

                    isWordReady = true;
                }
            }
        }).start();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Word Cloud 띄워주는 스레드
        // asset 폴더의 d3.html 참조
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isWordReady){ }
                if(isWordReady){
                    getActivity().runOnUiThread(()->{
                        progressBar.setVisibility(View.GONE);
                        d3 = (WebView) root.findViewById(R.id.d3_cloud);
                        d3.addJavascriptInterface(new AndroidBridge(), "GetWord");
                        WebSettings ws = d3.getSettings();
                        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                        ws.setJavaScriptEnabled(true);
                        ws.setLoadWithOverviewMode(true);
                        ws.setUseWideViewPort(true);

                        d3.setOnTouchListener(new OnClickWithOnTouchListener(root.getContext(), new OnClickWithOnTouchListener.OnClickListener() {
                            @Override
                            public void onClick() {
                                //Toast.makeText(getContext(),"WebViewClickTest",Toast.LENGTH_SHORT).show();

                            }
                        }));

                        d3.loadUrl("file:///android_asset/d3.html");
                        d3.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                StringBuffer sb = new StringBuffer();
                                sb.append("wordCloud([");
                                for (int i = 0; i < wordCloud.length; i++) {
                                    sb.append("'").append(wordCloud[i]).append("'");
                                    if (i < wordCloud.length - 1) {
                                        sb.append(",");
                                    }
                                }
                                sb.append("])");

                                d3.evaluateJavascript(sb.toString(), null);

                            }
                        });
                    });
                }
            }
        }).start();
    }

    // 서버에 등록된 설문의 pageCount 를 어디까지 상한을 둬야할지는 후에 테스트에서 실험해야할 듯.
    // 서버에서 연산을 해주는 방법도 고려해 볼 수 있습니다.
    public void getSurveyTitles(){
        RetrofitApi.getService().getSurveyList("all",pageCount).enqueue(new retrofit2.Callback<ArrayList<SurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<SurveyDTO>> call, retrofit2.Response<ArrayList<SurveyDTO>> response) {
                if(!response.body().isEmpty()){
                    datas.addAll(response.body());
                    //Toast.makeText(getContext(), Integer.toString(datas.size()),Toast.LENGTH_LONG).show();
                    pageCount++;
                    getSurveyTitles();
                }
                else {isFinish = true;}
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) {Toast.makeText(getContext(), "fail",Toast.LENGTH_LONG).show();}
        });

    }

    public ArrayList<String> extractTitleFromSurveyDTO(ArrayList<SurveyDTO> datas){
        ArrayList<String> arrayList = new ArrayList<>();
        String node;

        for(int position = 0; position  < datas.size(); position++){
            node = datas.get(position).getTitle();
            arrayList.add(node);
        }

        return arrayList;
    }

    public class UserSensorListner implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()){
                //자이로센서
                case Sensor.TYPE_GYROSCOPE:
                    //센서 값을 mGyroValues에 저장
                    mGyroValues = event.values;
                    if(!gyroRunning)
                        gyroRunning = true;

                    break;

                //가속도센서
                case Sensor.TYPE_ACCELEROMETER:
                    //센서 값을 mAccValues에 저장
                    mAccValues = event.values;
                    if(!accRunning)
                        accRunning = true;

                    break;
            }

            //두 센서 모두 등록되면 상보필터 적용
            if(gyroRunning && accRunning){
                complementaty(event.timestamp);
                // 센서값 d3.html 에 전달
                if(d3 != null){
                    StringBuffer sb = new StringBuffer();
                    sb.append("update(");
                    sb.append((int)pitch);
                    sb.append(")");

                    d3.evaluateJavascript(sb.toString(), null);

                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    }

    //자이로센서 적분값근사에 의한 오차를 가속도센서를 이용하여 어느정도 보정하는 상보필터 함수
    private void complementaty(double new_ts){

        // 센서 해제
        gyroRunning = false;
        accRunning = false;

        /*센서 값 첫 출력시 dt(=timestamp - event.timestamp)에 오차가 생기므로 처음엔 break */
        if(timestamp == 0){
            timestamp = new_ts;
            return;
        }
        dt = (new_ts - timestamp) * NS2S; // ns->s 변환
        timestamp = new_ts;

        /* degree measure for accelerometer */
        mAccPitch = -Math.atan2(mAccValues[0], mAccValues[2]) * 180.0 / Math.PI; // Y 축 기준
        mAccRoll= Math.atan2(mAccValues[1], mAccValues[2]) * 180.0 / Math.PI; // X 축 기준

        /**
         * 1st complementary filter.
         *  mGyroValuess : 각속도 성분.
         *  mAccPitch : 가속도계를 통해 얻어낸 회전각.
         */

        temp = (1/a) * (mAccPitch - pitch) + mGyroValues[1];
        pitch = pitch + (temp*dt);

        temp = (1/a) * (mAccRoll - roll) + mGyroValues[0];
        roll = roll + (temp*dt);

        // Word Y축 기울기값 임시로 제한. 자세에 따른 보정를 위한 함수 필요
        if(pitch > 40.0)
            pitch = 40.0;
        if(pitch < -40.0)
            pitch = -40.0;

    }

    //센서 등록 및 해제 체크
    private void checkGyroAcc(){
        if(!running){
            running = true;
            mSensorManager.registerListener(userSensorListner, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(userSensorListner, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        else if(running)
        {
            running = false;
            mSensorManager.unregisterListener(userSensorListner);

        }
    }
}
