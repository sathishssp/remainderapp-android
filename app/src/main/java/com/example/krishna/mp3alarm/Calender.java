package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;



public class Calender extends AppCompatActivity {

    CalendarView calender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

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
                Toast.makeText(context, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                String datenow = dayOfMonth + "/" + month + "/" + year;
                Intent intent = new Intent(Calender.this, Reminderpage.class);
                intent.putExtra("dayOfMonth", dayOfMonth);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });
    }
}
