package com.example.graduationproject.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MessagingService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String messageBody){
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        String channelId=getString(R.string.notification_channel_id);
        String channelName=getString(R.string.notification_channel_name);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.fab_item)
                .setContentTitle("알림이 왔어요!")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0,builder.build());
    }
}
