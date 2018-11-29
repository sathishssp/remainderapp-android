package com.example.krishna.mp3alarm.viewtwenty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.krishna.mp3alarm.controllertwenty.AlarmsManager;
import com.example.krishna.mp3alarm.modeltwenty.Alarm;
import com.example.krishna.mp3alarm.viewtwenty.activity.RingingAlarmActivity;

public class PlayAlarmReceiver extends BroadcastReceiver {

	// Notification ID to allow for future updates
	public static final int RINGING_NOTIFICATION_ID1 = 1;

	// private long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			onReboot(context);
			return;
		}

		long alarmId = intent.getLongExtra(Alarm.INTENT_ID, -1);

		Intent startRingingActivityIntent = new Intent(context,
				RingingAlarmActivity.class);

		startRingingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startRingingActivityIntent.putExtra(Alarm.INTENT_ID, alarmId);
		context.startActivity(startRingingActivityIntent);
	}

	private void onReboot(Context context) {

		// Intent onRebootIntent = new Intent(context, OnRebootActivity.class);
		// onRebootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(onRebootIntent);

		AlarmsManager alarmsManager = new AlarmsManager(context);

		Log.i("MP3ALARM",
				"OnRebootActivity, recreating " + alarmsManager.getAllAlarms()
						+ " alarms.");

		for (Alarm alarm : alarmsManager.getAllAlarms()) {
			alarmsManager.setAlarm(alarm, true, false);
		}
	}
}
