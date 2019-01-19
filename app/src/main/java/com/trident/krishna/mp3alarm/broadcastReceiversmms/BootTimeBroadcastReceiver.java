package com.trident.krishna.mp3alarm.broadcastReceiversmms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.trident.krishna.mp3alarm.alarmManagermms.ManagerOfAlarms;

public class BootTimeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ManagerOfAlarms alarmManager = new ManagerOfAlarms(context);
        alarmManager.setBootTimeAlarms();
    }
}
