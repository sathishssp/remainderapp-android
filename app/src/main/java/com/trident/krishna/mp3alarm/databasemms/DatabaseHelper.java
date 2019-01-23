package com.trident.krishna.mp3alarm.databasemms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trident.krishna.mp3alarm.databasemms.model.AlarmHistoryDBModel;
import com.trident.krishna.mp3alarm.databasemms.model.MyAlarmsDBModel;

/**
 * Created by Shoukhin on 7/4/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "device_scheduling_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyAlarmsDBModel.CREATE_TABLE);
        db.execSQL(AlarmHistoryDBModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyAlarmsDBModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmHistoryDBModel.TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    public long insertMyAlarm(MyAlarmsDBModel myAlarmsDBModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyAlarmsDBModel.COLUMN_PHONE, myAlarmsDBModel.getPhone());
        values.put(MyAlarmsDBModel.COLUMN_MESSAGE, myAlarmsDBModel.getMessage());
        values.put(MyAlarmsDBModel.COLUMN_IMAGE, myAlarmsDBModel.getImage());
        values.put(MyAlarmsDBModel.COLUMN_SOUND, myAlarmsDBModel.getSound());
        values.put(MyAlarmsDBModel.COLUMN_PHONE_STATUS, myAlarmsDBModel.getPhoneStatus());
        values.put(AlarmHistoryDBModel.COLUMN_TIMESTAMP, myAlarmsDBModel.getTimestamp());

        long id = db.insert(MyAlarmsDBModel.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long insertAlarmHistory(AlarmHistoryDBModel alarmHistoryDBModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AlarmHistoryDBModel.COLUMN_PHONE, alarmHistoryDBModel.getPhone());
        values.put(AlarmHistoryDBModel.COLUMN_MESSAGE, alarmHistoryDBModel.getMessage());
        values.put(AlarmHistoryDBModel.COLUMN_IMAGE, alarmHistoryDBModel.getImage());
        values.put(AlarmHistoryDBModel.COLUMN_SOUND, alarmHistoryDBModel.getSound());
        values.put(AlarmHistoryDBModel.COLUMN_PHONE_STATUS, alarmHistoryDBModel.getPhoneStatus());
        values.put(AlarmHistoryDBModel.COLUMN_TIMESTAMP, alarmHistoryDBModel.getTimestamp());

        long id = db.insert(AlarmHistoryDBModel.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Cursor getAllScheduleHistory() {
        String RAW_QUERY = "select * from " + AlarmHistoryDBModel.TABLE_NAME + " ORDER BY "
                + AlarmHistoryDBModel.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(RAW_QUERY, null);
    }

    public Cursor getMyHistory() {
        String RAW_QUERY = "select * from " + MyAlarmsDBModel.TABLE_NAME + " ORDER BY "
                + MyAlarmsDBModel.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(RAW_QUERY, null);
    }

    public Cursor getDetailInfo(int id, String tableName) {
        String RAW_QUERY = "select * from " + tableName + " where id=" + id;
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(RAW_QUERY, null);
    }
}
