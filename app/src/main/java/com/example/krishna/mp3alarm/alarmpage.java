package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class alarmpage extends AppCompatActivity {

    TextView general,alarmone,alarmtwelve,alarmtwentyfour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpage);

        general = (TextView)findViewById(R.id.generalalarm);
        alarmone = (TextView)findViewById(R.id.alaramone);
        alarmtwelve = (TextView)findViewById(R.id.alarmtwelve);
        alarmtwentyfour = (TextView)findViewById(R.id.alarmtwentyfour);

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarmpage.this,com.example.krishna.mp3alarm.view.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarmpage.this,com.example.krishna.mp3alarm.viewone.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmtwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarmpage.this,com.example.krishna.mp3alarm.viewtwelve.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmtwentyfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarmpage.this,com.example.krishna.mp3alarm.viewtwenty.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

    }
}
