package com.example.krishna.mp3alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button alarm,message,todo,record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        alarm = (Button)findViewById(R.id.alarm);
        message = (Button)findViewById(R.id.message);
        todo = (Button)findViewById(R.id.todo);
        record = (Button)findViewById(R.id.record);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SmsListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, com.example.krishna.mp3alarm.Maintodo.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, com.example.krishna.mp3alarm.view.activity.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, com.example.krishna.mp3alarm.active.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
