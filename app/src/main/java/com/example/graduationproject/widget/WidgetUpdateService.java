package com.example.graduationproject.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.graduationproject.widget.HomeSurveyWidget.ACTION_REFRESH_PRESSED;

public class WidgetUpdateService extends Service {

    private String surveyTitle = "default";
    private ArrayList<UploadedSurveyDTO> refreshSurveyArrList = new ArrayList<>();
    private boolean isRefreshPressed = false;
    private boolean isRefreshFinished = false;
    private boolean isRefreshFuncEnd = false;
    private static SharedPreferences preferences;
    static RemoteViews widget;

    IBinder mBinder = new MyBinder();
    class MyBinder extends Binder {
        WidgetUpdateService getService(){
            return WidgetUpdateService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("WUS_onCreate()","WUS_onCreate()");
        preferences = getSharedPreferences("testPref", Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("onStartCommand", "onStartCommand");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        ComponentName thisWidget = new ComponentName(this, HomeSurveyWidget.class);

        for(int appWidgetId : appWidgetIds){
            RemoteViews remoteViews = buildUpdate(this, appWidgetId);
            appWidgetManager.updateAppWidget(thisWidget,remoteViews);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private RemoteViews buildUpdate(Context context, int appWidgetId){
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);
        setPressRefreshButton(context, updateViews, appWidgetId);
        refresh(context, updateViews);

        return updateViews;
    }

    private String getTime(String str){
        long now;
        Date date;
        if(str != null) {
            now = Long.valueOf(str);
            date = new Date(now);
        }
        else
            date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 MM월 dd일");
        String time = simpleDate.format(date);
        return time;
    }

    private synchronized void refresh(Context context, RemoteViews remoteViews){

        if(preferences.getBoolean("isRefreshPressed", false)){
            isRefreshPressed = true;
        }
        Log.d("isRefreshPressed",Boolean.toString(isRefreshPressed));

        Thread refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int recv_response;
                int recv_expect;
                String time;

                if(isRefreshPressed){
                    if(preferences.getInt("_id", -1) == -1)
                        return;
                    Log.d("getInt_id", Integer.toString(preferences.getInt("_id", 0)));
                    get_idSurveyList(preferences.getInt("_id", 0));

                    while(!isRefreshFinished);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("_id", refreshSurveyArrList.get(0).get_id());
                    editor.putString("title", refreshSurveyArrList.get(0).getTitle());
                    editor.putInt("response", refreshSurveyArrList.get(0).getResponse_cnt());
                    editor.putString("time",refreshSurveyArrList.get(0).getTime());
                    editor.apply();

                    recv_response = refreshSurveyArrList.get(0).getResponse_cnt();
                    recv_expect = preferences.getInt("expectValue",0);
                    time = getTime(refreshSurveyArrList.get(0).getTime());
                }
                else{
                    Log.d("getInt_id2", Integer.toString(preferences.getInt("_id", 0)));
                    recv_response = preferences.getInt("response", 0);
                    recv_expect = preferences.getInt("expectValue", 0);
                    time = getTime(preferences.getString("time", null));
                }

                if(recv_expect != 0){
                    int value = ((recv_response * 100) / recv_expect);
                    if(value > 100){value = 100;}
                    surveyTitle = preferences.getString("title","fail");
                    Log.i("preftest",surveyTitle);
                    Log.i("recv_expect",Integer.toString(recv_expect));
                    Log.i("value",Integer.toString(value));
                    remoteViews.setProgressBar(R.id.widget_progressBar,100, value, false);
                    remoteViews.setTextViewText(R.id.appwidget_text, surveyTitle);
                    remoteViews.setTextViewText(R.id.widget_txtProgress, Integer.toString(value) + "%");
                    remoteViews.setTextViewText(R.id.homewidget_response, Integer.toString(recv_response) + "명 응답");
                    remoteViews.setTextViewText(R.id.homewidget_time, time);
                }
            }
        });
        refreshThread.start();

        try {
            refreshThread.join();
        }catch (InterruptedException e){}

        isRefreshPressed = false;
        isRefreshFinished = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRefreshPressed", false);
        editor.apply();
    }

    private void get_idSurveyList(Integer id){
        RetrofitApi.getService().get_idSurveyList(id).enqueue(new retrofit2.Callback<ArrayList<UploadedSurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, retrofit2.Response<ArrayList<UploadedSurveyDTO>> response) {
                refreshSurveyArrList.clear();
                refreshSurveyArrList.addAll(response.body());
                isRefreshFinished= true;
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, Throwable t) { }
        });
    }

    private void setPressRefreshButton(Context context, RemoteViews remoteViews, int appwidgetId){
        Intent intent = new Intent(context, HomeSurveyWidget.class);
        intent.setAction(ACTION_REFRESH_PRESSED);
        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.homewidget_refresh, pendingIntent);
    }
}
