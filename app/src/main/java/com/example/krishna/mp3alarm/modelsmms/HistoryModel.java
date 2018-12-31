package com.example.krishna.mp3alarm.modelsmms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shoukhin on 7/9/2018.
 */

public class HistoryModel {
    private int id;
    private int imageResourceID;
    private String message;
    private String setterName;
    private String timestamp;
    private String tableName;

    public HistoryModel() {

    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFormattedDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
                Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    public String getFormattedTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a",
                Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }
}
