package com.example.krishna.mp3alarm.Utility;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;


import com.example.krishna.mp3alarm.BuildConfig;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    public static boolean cardProcessRunning=false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 2000, 5 * 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            Log.e("Log", "Running");

        }
    };

    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.example.krishna.mp3alarm");
        intent.putExtra("yourvalue", "start");
        intent.setAction("com.example.krishna.mp3alarm.START");
        sendBroadcast(intent);
    }
}