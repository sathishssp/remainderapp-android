package com.trident.krishna.mp3alarm.resourceManagermms;

import android.media.AudioManager;

/**
 * Created by Shoukhin on 7/9/2018.
 */

public class ResourceManager {
    public static final Integer[] imageResources = {com.trident.krishna.mp3alarm.R.drawable.pic1, com.trident.krishna.mp3alarm.R.drawable.pic2,
            com.trident.krishna.mp3alarm.R.drawable.pic3, com.trident.krishna.mp3alarm.R.drawable.pic4, com.trident.krishna.mp3alarm.R.drawable.pic7, com.trident.krishna.mp3alarm.R.drawable.pic8, com.trident.krishna.mp3alarm.R.drawable.pic9,
            com.trident.krishna.mp3alarm.R.drawable.pic10, com.trident.krishna.mp3alarm.R.drawable.pic11, com.trident.krishna.mp3alarm.R.drawable.pic14, com.trident.krishna.mp3alarm.R.drawable.pic13,
            com.trident.krishna.mp3alarm.R.drawable.pic6, com.trident.krishna.mp3alarm.R.drawable.pic5, com.trident.krishna.mp3alarm.R.drawable.pic14, com.trident.krishna.mp3alarm.R.drawable.pic15,
    };
    public static final int[] soundResources = {com.trident.krishna.mp3alarm.R.raw.sound1, com.trident.krishna.mp3alarm.R.raw.sound2, com.trident.krishna.mp3alarm.R.raw.sound3,
            com.trident.krishna.mp3alarm.R.raw.sound4, com.trident.krishna.mp3alarm.R.raw.sound5, com.trident.krishna.mp3alarm.R.raw.sound6, com.trident.krishna.mp3alarm.R.raw.sound7, com.trident.krishna.mp3alarm.R.raw.sound8,
            com.trident.krishna.mp3alarm.R.raw.sound9
    };
    public static final String[] soundNames = {"Crazy Smile", "Missile Alert", "Analog Alarm",
            "Ringtone 1", "Ringtone 2", "Ringtone 3", "Alarm 1", "Eluveitie isara"};

    public static int mapImageResource(int imageResourceID) {
        for (int i = 0; i < imageResources.length; i++) {
            if (imageResourceID == imageResources[i])
                return i;
        }
        return 0;
    }

    public static int getMappedImageResourceID(int imageResourceID) {
        if (imageResourceID >= imageResources.length)
            return imageResources[0];

        return imageResources[imageResourceID];
    }

    public static int getSoundFromID(int soundResourceID) {
        if (soundResourceID >= soundResources.length)
            return soundResources[0];

        return soundResources[soundResourceID];
    }

    public static int mapPhoneStatusFromName(String status) {
        switch (status) {
            case "Silent":
                return AudioManager.RINGER_MODE_SILENT;
            case "Vibrate":
                return AudioManager.RINGER_MODE_VIBRATE;
            case "Ringing":
                return AudioManager.RINGER_MODE_NORMAL;
            default:
                return -1;
        }
    }
}
