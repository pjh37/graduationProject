package com.example.graduationproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class HomeSurveyWidget extends AppWidgetProvider {

    public static String WIDGET_LOGIN_ACTION = "WidgetLoginAction";
    public static final String ACTION_REFRESH_PRESSED = ".widget.HomeSurveyWidget.ACTION_REFRESH_PRESSED";
    public static final String ACTION_CONFIGURE_FINISHED = ".widget.HomeSurveyWidget.ACTION_CONFIGURE_FINISHED";
    private String surveyTitle = "default";
    private ArrayList<UploadedSurveyDTO> refreshSurveyArrList = new ArrayList<>();
    private boolean isRefreshPressed = false;
    private boolean isRefreshFinished = false;
    private static SharedPreferences preferences;
    static RemoteViews widget;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        widget = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);
        // Instruct the widget manager to update the widget
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, widget.getLayoutId());
        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onReceive(Context context, Intent recv_intent){
        String action = recv_intent.getAction();
        if(action != null && action.equals(ACTION_REFRESH_PRESSED)){
            preferences = context.getSharedPreferences("testPref",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isRefreshPressed", true);
            editor.commit();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            startUpdateService(context, appWidgetManager);

        }
        super.onReceive(context,recv_intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        widget = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            startUpdateService(context, appWidgetManager);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        widget = new RemoteViews(context.getPackageName(), R.layout.home_survey_widget);

        if(preferences == null)
            preferences = context.getSharedPreferences("testPref",Context.MODE_PRIVATE);
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
        context.stopService(intent);
        super.onDisabled(context);
    }

    private void startUpdateService(Context context, AppWidgetManager appWidgetManager){
        ComponentName componentName = new ComponentName(context, HomeSurveyWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }

}

