package com.example.krishna.mp3alarm.view;

import com.example.krishna.mp3alarm.R;
import com.example.krishna.mp3alarm.Utility.NotificationUtils;
import com.example.krishna.mp3alarm.controller.AlarmsManager;
import com.example.krishna.mp3alarm.model.Alarm;
import com.example.krishna.mp3alarm.view.activity.RingingAlarmActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MediaPlayerService extends Service {

	private AlarmsManager alarmsManger;
	private final CharSequence tickerText = "Rise and shine";
	private final CharSequence contentTitle = "mp3 alarm clock";
	public static final int RINGING_NOTIFICATION_ID = 111;
	public static String START_PLAY = "START_PLAY";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null && intent.getBooleanExtra(START_PLAY, false)) {
			long alarmId = intent.getLongExtra(Alarm.INTENT_ID, -1);
			playMusicAndShowNotification(alarmId);
		}
		return Service.START_REDELIVER_INTENT;
	}

	private void playMusicAndShowNotification(long alarmId) {

		alarmsManger = new AlarmsManager(getApplicationContext());
		alarmsManger.playMusic(alarmId);

		Intent restartRingingActivityIntent = new Intent(
				getApplicationContext(), RingingAlarmActivity.class);
		restartRingingActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		restartRingingActivityIntent.putExtra(Alarm.INTENT_ID, alarmId);
		PendingIntent restartRingingActivityPenInt = PendingIntent.getActivity(
				getApplicationContext(), (int) System.currentTimeMillis(),
				restartRingingActivityIntent, 0);

		/*Notification notiBuilder = new Notification.Builder(
				getApplicationContext()).setTicker(tickerText)
				.setSmallIcon(R.drawable.ic_action_onoff_pressed)
				.setOngoing(true)
				.setContentIntent(restartRingingActivityPenInt)
				.setContentTitle(contentTitle).setContentText("Now ringing")
				.build();

		NotificationManager notiMng = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notiMng.cancel(RINGING_NOTIFICATION_ID);
		notiMng.cancel(RingingAlarmActivity.SNOOZING_NOTIFICATION_ID);*/

		NotificationUtils.showNotification(this,restartRingingActivityPenInt,-1,contentTitle.toString(),
				"Now Ringing",R.drawable.ic_action_onoff_pressed);

//		startForeground(RINGING_NOTIFICATION_ID, notiBuilder);
	}

	@Override
	public void onDestroy() {
		stop();
	}

	private void stop() {
		alarmsManger.stopMusic();
		stopForeground(true);
	}

}
