package com.example.krishna.mp3alarm.alarmManagermms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.krishna.mp3alarm.Constantsmms;
import com.example.krishna.mp3alarm.databasemms.DatabaseHelper;
import com.example.krishna.mp3alarm.databasemms.model.AlarmHistoryDBModel;

import java.util.Calendar;

/**
 * Created by Shoukhin on 7/5/2018.
 */

public class ManagerOfAlarms {
    private Context context;

    public ManagerOfAlarms(Context context) {
        this.context = context;
    }

    public void setAlarm(String time, int id) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(Long.parseLong(time));
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarms.class);
        i.putExtra(Constantsmms.ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (am != null) {
            am.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
        }
    }

    public void setBootTimeAlarms() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor schedules = databaseHelper.getAllScheduleHistory();

        for (schedules.moveToFirst(); !schedules.isAfterLast(); schedules.moveToNext()) {
            int id = schedules.getInt
                    (schedules.getColumnIndex(AlarmHistoryDBModel.COLUMN_ID));
            String timeStamp = schedules.getString
                    (schedules.getColumnIndex(AlarmHistoryDBModel.COLUMN_TIMESTAMP));
            Calendar currentTime = Calendar.getInstance();

            if (currentTime.getTimeInMillis() >= Long.parseLong(timeStamp)) {
                setAlarm(timeStamp, id);
            }
        }
    }
}
