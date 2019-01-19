package com.trident.krishna.mp3alarm;

public class ReminderReceiver extends WakefulBroadcastReceiver {

    @Override
    protected Class getServiceClass() {
        return ReminderService.class;
    }
}
