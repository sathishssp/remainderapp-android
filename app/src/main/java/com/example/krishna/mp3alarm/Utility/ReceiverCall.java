package com.example.krishna.mp3alarm.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverCall extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
		Log.e("Service Restarted", "Ohhhhhhh");
		context.startService(new Intent(context, BackgroundService.class));
	}

}