package com.trident.krishna.mp3alarm;

public class SmsSenderReceiver extends WakefulBroadcastReceiver {

    @Override
    protected Class getServiceClass() {
        return SmsSenderService.class;
    }
}
