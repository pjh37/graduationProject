package com.example.graduationproject.commentFCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.graduationproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MsgService";

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (true) {
            } else {
                handleNow();
            }
        }

        if (remoteMessage.getData() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getData().get("title"));
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("body"));
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
        }
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(String messageTitle, String messageBody) {

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("description");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(false);
            channel.setVibrationPattern(new long[] {100, 200, 100, 200});
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(8888, notificationBuilder.build());
    }

    private void sendRegistrationToServer(String token){

    }
}
