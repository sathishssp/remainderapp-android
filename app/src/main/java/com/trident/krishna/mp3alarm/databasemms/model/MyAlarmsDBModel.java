package com.trident.krishna.mp3alarm.databasemms.model;

/**
 * Created by Shoukhin on 7/4/2018.
 */

public class MyAlarmsDBModel {
    public static final String TABLE_NAME = "myalarms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_SOUND = "sound";
    public static final String COLUMN_PHONE_STATUS = "phonestatus";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_MESSAGE + " TEXT,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_SOUND + " TEXT,"
                    + COLUMN_PHONE_STATUS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    private int id;
    private String phone;
    private String message;
    private String image;
    private String sound;
    private String phoneStatus;
    private String timestamp;

    public MyAlarmsDBModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(String phoneStatus) {
        this.phoneStatus = phoneStatus;
    }
}
