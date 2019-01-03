package com.example.krishna.mp3alarm.Utility;

import android.content.Context;
import android.content.res.TypedArray;

import com.example.krishna.mp3alarm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utils {

    public static String lastSelectedDate="",lastRecordedAudioFilePath=null;
    public static String btnLabel="Add Alarm";


    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
