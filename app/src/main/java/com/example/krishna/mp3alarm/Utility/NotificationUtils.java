package com.example.krishna.mp3alarm.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.example.krishna.mp3alarm.view.MediaPlayerService;

public class NotificationUtils {

    public static final int SNOOZING_NOTIFICATION_ID = 222;
    public static void showNotification(Context context, PendingIntent intent,int sound,String title,String msg,int drawable){

        String NOTIFICATION_CHANNEL_ID = "myAlarm";
        String channelName = "My Alarm Service";

        NotificationChannel chan = null;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        NotificationCompat.Builder notificationBuilder=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(chan);
            notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }

        Notification notification = notificationBuilder.setOngoing(true)
                .setTicker(msg)
                .setSmallIcon(drawable)
                .setContentIntent(intent)
                .setContentTitle(title)
                .setContentText(msg).build();
//        manager.cancel(MediaPlayerService.RINGING_NOTIFICATION_ID);
//        manager.cancel(SNOOZING_NOTIFICATION_ID);

        manager.notify(SNOOZING_NOTIFICATION_ID, notification);
//        context.startF
    }
}
