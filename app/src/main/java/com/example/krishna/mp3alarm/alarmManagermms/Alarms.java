package com.example.krishna.mp3alarm.alarmManagermms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.krishna.mp3alarm.Constantsmms;
import com.example.krishna.mp3alarm.ContentShowmms;
import com.example.krishna.mp3alarm.databasemms.model.AlarmHistoryDBModel;

/**
 * Created by Shoukhin on 5/8/2018.
 */

public class Alarms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = null;
        if (pm != null) {
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wl.acquire(10 * 60 * 1000L /*10 minutes*/);
        }

        Bundle extras = intent.getExtras();
        int id = 0;
        if (extras != null) {
            id = extras.getInt(Constantsmms.ID);
        }

        Intent i = new Intent(context, ContentShowmms.class);
        i.putExtra(Constantsmms.ID, id);
        i.putExtra(Constantsmms.TABLE_NAME, AlarmHistoryDBModel.TABLE_NAME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(Constantsmms.RECEIVER, Constantsmms.FROM);
        i.putExtra(Constantsmms.PREVIEW, false);
        context.startActivity(i);

        wl.release();
    }
}
