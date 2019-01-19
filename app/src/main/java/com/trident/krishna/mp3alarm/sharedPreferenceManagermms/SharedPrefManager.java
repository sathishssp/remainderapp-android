package com.trident.krishna.mp3alarm.sharedPreferenceManagermms;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import java.util.Map;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "devicescheduling";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveAllowedNumber(String number) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(number, true);
        editor.apply();
    }

    public boolean isAllowed(String number) {
        Map<String, ?> allowedNumbers = getAllAllowedNumbers();
        for (Map.Entry<String, ?> allowedNumber : allowedNumbers.entrySet()) {
            if (PhoneNumberUtils.compare(allowedNumber.getKey(), number)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, ?> getAllAllowedNumbers() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    public void removeValue(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public String getNameFromNumber(String SMSSenderNumber) {
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.
                CONTENT_FILTER_URI, Uri.encode(SMSSenderNumber));
        Cursor c = mCtx.getContentResolver().query(lookupUri,
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