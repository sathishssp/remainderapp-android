package com.trident.krishna.mp3alarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.trident.krishna.mp3alarm.Utility.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.riontech.calendar.CustomCalendar;
import com.riontech.calendar.dao.EventData;
import com.riontech.calendar.dao.dataAboutDate;
import com.riontech.calendar.utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class Calender extends AppCompatActivity {

    CalendarView calender;
    CustomCalendar customCalendar;
    private ArrayList<SmsModel> dbList;
    private ArrayList<EventData> eventList;
    CompactCalendarView compactCalendarView;
    TextView txtMonthYear;
    String fromScreen="";
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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

        if(getIntent().hasExtra("FROM")){
            fromScreen=getIntent().getStringExtra("FROM");
        }

        customCalendar=findViewById(R.id.customCalendar);

        txtMonthYear=findViewById(R.id.txt_month_year);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);

        Event ev1 = new Event(Color.GREEN, 1545840918660L);
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        MobileAds.initialize(this,"ca-app-pub-1557256197043076~4808962026");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
//                if (dateClicked.getTimestampScheduled() < GregorianCalendar.getInstance().getTimeInMillis()) {
//                    Toast.makeText(getApplicationContext(), getString(R.string.form_validation_datetime), Toast.LENGTH_SHORT).show();
//                    result = false;
//                }
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d("Calendar", "Day was clicked: " + dateClicked + " with events " + events);
                String datenow = dateFormatForDisplaying.format(dateClicked);
                Utils.lastSelectedDate=datenow;
                if(fromScreen.equalsIgnoreCase("R")) {
                    Intent intent = new Intent(Calender.this, Reminderpage.class);
                    startActivity(intent);
                    finish();
                } else if(fromScreen.equalsIgnoreCase("TODO")){
                    Intent intent = new Intent(Calender.this, com.trident.krishna.mp3alarm.Maintodo.MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Calender.this, Reminderpage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("Calendar", "Month was scrolled to: " + firstDayOfNewMonth);
                txtMonthYear.setText((dateFormatForMonth.format(firstDayOfNewMonth)));
            }
        });

        txtMonthYear.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        dbList=DbHelper.getDbHelper(this).getAllEvents();
        eventList=new ArrayList<>();
        for(SmsModel model:dbList){
            Event ev= new Event(Color.GREEN, model.getTimestampScheduled());
            compactCalendarView.addEvent(ev);
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
