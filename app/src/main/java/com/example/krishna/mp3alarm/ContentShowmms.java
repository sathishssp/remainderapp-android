package com.example.krishna.mp3alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krishna.mp3alarm.databasemms.DatabaseHelper;
import com.example.krishna.mp3alarm.databasemms.model.AlarmHistoryDBModel;
import com.example.krishna.mp3alarm.resourceManagermms.ResourceManager;

public class ContentShowmms extends AppCompatActivity {
    ImageView imageView;
    TextView receiverTextView;
    TextView messageTextView;
    MediaPlayer mediaPlayer = null;

    int soundResourceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.krishna.mp3alarm.R.layout.activity_content_showmms);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        imageView = findViewById(com.example.krishna.mp3alarm.R.id.content_image_view);
        messageTextView = findViewById(com.example.krishna.mp3alarm.R.id.message_text_view);
        receiverTextView = findViewById(com.example.krishna.mp3alarm.R.id.receiver_text_view);
        Bundle bundle = getIntent().getExtras();

        if (bundle == null)
            return;

        int id = bundle.getInt(Constantsmms.ID);
        String tableName = bundle.getString(Constantsmms.TABLE_NAME);
        String receiverText = bundle.getString(Constantsmms.RECEIVER);
        boolean isPreview = bundle.getBoolean(Constantsmms.PREVIEW);

        receiverTextView.setText(receiverText);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor detailInfo = databaseHelper.getDetailInfo(id, tableName);
        detailInfo.moveToFirst();

        String tempImageResourceID = detailInfo.getString
                (detailInfo.getColumnIndex(AlarmHistoryDBModel.COLUMN_IMAGE));
        int imageResourceID = getImageResourceId(tempImageResourceID);

        String tempSoundResourceID = detailInfo.getString
                (detailInfo.getColumnIndex(AlarmHistoryDBModel.COLUMN_SOUND));
        soundResourceID = getSoundResourceId(tempSoundResourceID);

        String message = detailInfo.getString
                (detailInfo.getColumnIndex(AlarmHistoryDBModel.COLUMN_MESSAGE));

        String senderPhoneNumber = detailInfo.getString
                (detailInfo.getColumnIndex(AlarmHistoryDBModel.COLUMN_PHONE));
        String senderName = getSMSSenderName(senderPhoneNumber);

        String tempPhoneStatus = detailInfo.getString
                (detailInfo.getColumnIndex(AlarmHistoryDBModel.COLUMN_PHONE_STATUS));
        int phoneStatus = Integer.parseInt(tempPhoneStatus);


        imageView.setImageResource(imageResourceID);
        messageTextView.setText(message);
        setSenderPhoneAndNameText(senderPhoneNumber, senderName);

        if (!isPreview)
            changePhoneStatus(phoneStatus);

        playSound(soundResourceID);
        setVolume(phoneStatus);
    }

    private void setVolume(int phoneStatus) {
        int defaultPhoneStatus = -1;
        if (phoneStatus == AudioManager.RINGER_MODE_NORMAL)
            mediaPlayer.setVolume(1.0f, 1.0f);
        else if (phoneStatus == defaultPhoneStatus) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            if (audioManager != null && audioManager.getRingerMode()
                    == AudioManager.RINGER_MODE_NORMAL) {
                mediaPlayer.setVolume(1.0f, 1.0f);
            }
        }
    }

    private void setSenderPhoneAndNameText(String number, String name) {
        String nameAndPhone;
        TextView nameAndPhoneTextView = findViewById(com.example.krishna.mp3alarm.R.id.sender_name_text_view);

        if (TextUtils.isEmpty(name))
            nameAndPhone = number;
        else
            nameAndPhone = number + " (" + name + ")";

        nameAndPhoneTextView.setText(nameAndPhone);
    }

    private void playSound(int soundResourceID) {
        mediaPlayer = MediaPlayer.create(this, soundResourceID);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.start();
    }

    private void changePhoneStatus(int status) {

        // -1 for no change mode
        if (status == -1)
            return;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(status);
        }
    }

    private int getImageResourceId(String imageID) {
        int mappedImageResourceID = Integer.parseInt(imageID);
        int imageResourceID = ResourceManager.getMappedImageResourceID(mappedImageResourceID);
        return imageResourceID;
    }

    private int getSoundResourceId(String soundID) {
        int mappedSoundResourceID = Integer.parseInt(soundID);
        int soundResourceID = ResourceManager.getSoundFromID(mappedSoundResourceID);
        return soundResourceID;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer == null) {
            playSound(soundResourceID);
        }

    }

    private String getSMSSenderName(String SMSSenderNumber) {
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.
                CONTENT_FILTER_URI, Uri.encode(SMSSenderNumber));
        Cursor c = getContentResolver().query(lookupUri,
                new String[]{ContactsContract.Data.DISPLAY_NAME},
                null, null, null);
        try {
            c.moveToFirst();
            String displayName = c.getString(0);
            return displayName;

        } catch (Exception e) {
        } finally {
            c.close();
        }
        return "";
    }
}
