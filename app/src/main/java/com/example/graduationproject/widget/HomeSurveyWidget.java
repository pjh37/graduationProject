package com.example.graduationproject.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.graduationproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class HomeSurveyWidget extends AppWidgetProvider {

    public static String WIDGET_LOGIN_ACTION = "WidgetLoginAction";
    private String userEmail = "default";
    SharedPreferences preferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        preferences = context.getSharedPreferences("testPref",Context.MODE_PRIVATE);
        userEmail = preferences.getString("title","fail");

        Log.i("preftest",userEmail);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);
            //widget.setRemoteAdapter(R.id.appwidget_listview, serviceIntent);

            this.refresh(context, widget);
            //updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetIds, widget);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {

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

    private void refresh(Context context, RemoteViews remoteViews){
        remoteViews.setTextViewText(R.id.appwidget_text, userEmail);
        int recv_response = preferences.getInt("response",0);
        int recv_expect = preferences.getInt("expectValue",0);
        String time = getTime(preferences.getString("time",null));
        if(recv_expect != 0){
            int value = recv_response / recv_expect;
            if(value > 1){value = 1;}
            remoteViews.setTextViewText(R.id.widget_txtProgress, Integer.toString(value) + "%");
            remoteViews.setTextViewText(R.id.homewidget_response, Integer.toString(recv_response) + "명 응답");
            remoteViews.setTextViewText(R.id.homewidget_time, time);
        }
    }
    
}

