package com.example.krishna.mp3alarm;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.krishna.mp3alarm.adaptersmms.HistoryAdapter;
import com.example.krishna.mp3alarm.databasemms.DatabaseHelper;
import com.example.krishna.mp3alarm.databasemms.model.AlarmHistoryDBModel;
import com.example.krishna.mp3alarm.databasemms.model.MyAlarmsDBModel;
import com.example.krishna.mp3alarm.modelsmms.HistoryModel;
import com.example.krishna.mp3alarm.resourceManagermms.ResourceManager;
import com.example.krishna.mp3alarm.sharedPreferenceManagermms.SharedPrefManager;

import java.util.ArrayList;

public class MySchedulesmms extends AppCompatActivity {
    private ArrayList<HistoryModel> historyData = new ArrayList<>();
    RecyclerView historyRecyclerView;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.krishna.mp3alarm.R.layout.activity_alarm_historymms);

        historyRecyclerView = findViewById(com.example.krishna.mp3alarm.R.id.alarms_recycler_view);
        historyAdapter = new HistoryAdapter(historyData, this);
        historyAdapter.definedAsMySchedule();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        historyRecyclerView.setLayoutManager(mLayoutManager);
        historyRecyclerView.setAdapter(historyAdapter);
        getHistoryData();
    }

    private void getHistoryData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor scheduleHistory = databaseHelper.getMyHistory();
        while (scheduleHistory.moveToNext()) {
            HistoryModel historyModel = new HistoryModel();
            historyModel.setId(scheduleHistory.getInt
                    (scheduleHistory.getColumnIndex(MyAlarmsDBModel.COLUMN_ID)));

            historyModel.setTableName(MyAlarmsDBModel.TABLE_NAME);

            String tempImageResourceID = scheduleHistory.getString
                    (scheduleHistory.getColumnIndex(MyAlarmsDBModel.COLUMN_IMAGE));
            int mappedImageResourceID = ResourceManager.getMappedImageResourceID(
                    Integer.parseInt(tempImageResourceID));
            historyModel.setImageResourceID(mappedImageResourceID);

            historyModel.setMessage(scheduleHistory.getString
                    (scheduleHistory.getColumnIndex(MyAlarmsDBModel.COLUMN_MESSAGE)));

            String phoneNumber = scheduleHistory.getString
                    (scheduleHistory.getColumnIndex(AlarmHistoryDBModel.COLUMN_PHONE));
            historyModel.setSetterName(getPhoneNumberWithName(phoneNumber));

            String tempTimestamp = scheduleHistory.getString
                    (scheduleHistory.getColumnIndex(AlarmHistoryDBModel.COLUMN_TIMESTAMP));
            historyModel.setTimestamp(tempTimestamp);

            historyData.add(historyModel);
        }
        historyAdapter.notifyDataSetChanged();
    }

    private String getPhoneNumberWithName(String phoneNumber) {
        SharedPrefManager manager = SharedPrefManager.getInstance(this);
        String name = manager.getNameFromNumber(phoneNumber);
        if (!TextUtils.isEmpty(name))
            return phoneNumber + " (" + name + ")";

        return phoneNumber;
    }
}
