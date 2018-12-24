package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.krishna.mp3alarm.Utility.Utils;
import com.riontech.calendar.CustomCalendar;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;
import com.riontech.calendar.utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class Calender extends AppCompatActivity {

    CalendarView calender;
    CustomCalendar customCalendar;
    private ArrayList<SmsModel> dbList;
    private ArrayList<EventData> eventList;

    public ArrayList<EventData> getEventDataList(int count,SmsModel smsModel) {
        ArrayList<EventData> eventDataList = new ArrayList();

        for (int i = 0; i < count; i++) {
            EventData dateData = new EventData();
            ArrayList<dataAboutDate> dataAboutDates = new ArrayList();

            dateData.setSection(smsModel.getRecipientName());
            dataAboutDate dataAboutDate = new dataAboutDate();

            int index = new Random().nextInt(CalendarUtils.getEVENTS().length);

            dataAboutDate.setTitle(smsModel.getRecipientName());
            dataAboutDate.setSubject(smsModel.getMessage());
            dataAboutDates.add(dataAboutDate);

            dateData.setData(dataAboutDates);
            eventDataList.add(dateData);
        }

        return eventDataList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        customCalendar=findViewById(R.id.customCalendar);


        dbList=DbHelper.getDbHelper(this).getAllEvents();
        eventList=new ArrayList<>();
        for(SmsModel model:dbList){

            customCalendar.addAnEvent(Utils.getDate(model.getTimestampScheduled(),"yyyy-MM-dd"),1,getEventDataList(1,model));


        }






        calender = (CalendarView)findViewById(R.id.calendar);
        calender.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of  focused month
        calender.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        calender.setSelectedWeekBackgroundColor(Color.WHITE); // red color for the selected week's background
        calender.setWeekSeparatorLineColor(Color.GREEN);

        final android.content.Context context = this.getApplicationContext();
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                //display the selected date by using a toast
//                Toast.makeText(context, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                String datenow = dayOfMonth + "/" + month + "/" + year;
                Utils.lastSelectedDate=datenow;
                Intent intent = new Intent(Calender.this, Reminderpage.class);
                intent.putExtra("dayOfMonth", dayOfMonth);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });
    }
}
