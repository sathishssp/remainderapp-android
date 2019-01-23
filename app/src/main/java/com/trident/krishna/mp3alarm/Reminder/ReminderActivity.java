package com.trident.krishna.mp3alarm.Reminder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.trident.krishna.mp3alarm.AppDefault.AppDefaultActivity;
import com.trident.krishna.mp3alarm.R;

public class ReminderActivity extends AppDefaultActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.reminder_layout;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return ReminderFragment.newInstance();
    }


}
