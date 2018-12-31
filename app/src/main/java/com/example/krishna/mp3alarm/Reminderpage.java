package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.krishna.mp3alarm.Utility.Utils;


public class Reminderpage extends AppCompatActivity {

    TextView call,message,notification;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminderpage);

        call = (TextView)findViewById(R.id.setcall);
        message = (TextView)findViewById(R.id.setmessage);
        notification = (TextView)findViewById(R.id.setnotification);

        datePicker = (DatePicker)findViewById(R.id.datepicker);
        Intent mIntent = getIntent();
        int dayOfMonth = mIntent.getIntExtra("dayOfMonth",0);
        int month = mIntent.getIntExtra("month", 0);
        int year = mIntent.getIntExtra("year", 0);

        String[] selectedDate= Utils.lastSelectedDate.split("/");
        if(selectedDate!=null && selectedDate.length>0){
            dayOfMonth=Integer.parseInt(selectedDate[0]);
            month=Integer.parseInt(selectedDate[1]);
            year=Integer.parseInt(selectedDate[2]);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year,  month, dayOfMonth);
        int xxday = cal.get(Calendar.DATE);
        int xxmonth = cal.get(Calendar.MONTH);
        int xxyear = cal.get(Calendar.YEAR);
        datePicker.init(xxyear, xxmonth, xxday, null);
     /*   Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            new FancyShowCaseView.Builder(this)
                    .title("This page is to alarm for 1 hr,12hr,24hr")
                    .showOnce("id0")
                    .build()
                    .show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();*/

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Reminderpage.this,Homemms.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminderpage.this,SmsListActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminderpage.this,com.example.krishna.mp3alarm.Maintodo.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Reminderpage.this, com.example.krishna.mp3alarm.active.MainActivity.class);
        overridePendingTransition(R.anim.rotate, R.anim.rotate);
        startActivity(intent);
        finish();

    }
}
