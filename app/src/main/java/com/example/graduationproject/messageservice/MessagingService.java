package com.example.graduationproject.messageservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.login.Session;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MessagingService extends Service {
    Socket socket;
    String url;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("테스트","MessagingService onCreate 실행");

    }
    private Emitter.Listener onConnected=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //socket.emit("connectComplete","jjjj1352");
        }
    };
    private Emitter.Listener onMessageReceive=new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
    private void startForegroundService(){
        Log.v("테스트","startForegroundService 알림 실행");
        SharedPreferences login_info=getSharedPreferences("loginConfig",0);

        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("userEmail",login_info.getString("userEmail",""));
        intent.putExtra("userName",login_info.getString("userName",""));
        intent.putExtra("userImage",login_info.getString("userImage",""));
        Log.v("테스트","startForegroundService : "+login_info.getString("userEmail","")+"  "+login_info.getString("userName",""));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId=getString(R.string.notification_channel_id);
        String channelName=getString(R.string.notification_channel_name);
        NotificationCompat.Builder builder;
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder=new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.drawable.fab_item)
                    .setContentTitle("Checkmate 앱이 실행중입니다")
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent);
            NotificationChannel channel=new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }else{
            builder=new NotificationCompat.Builder(this);
        }
        startForeground(1,builder.build());
        notificationManager.notify(0,builder.build());
    }
}
