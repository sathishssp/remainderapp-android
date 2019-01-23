package com.trident.krishna.mp3alarm;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Homemms extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button setScheduleButton;
    int REQUEST_SMS_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trident.krishna.mp3alarm.R.layout.activity_homemms);
        Toolbar toolbar = (Toolbar) findViewById(com.trident.krishna.mp3alarm.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.trident.krishna.mp3alarm.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.trident.krishna.mp3alarm.R.string.navigation_drawer_open, com.trident.krishna.mp3alarm.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.trident.krishna.mp3alarm.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setScheduleButton = findViewById(com.trident.krishna.mp3alarm.R.id.set_schedule_button);

        setScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homemms.this, AddSchedulemms.class));
            }
        });

        requestPermissions();
        requestDoNotDisturbPermission();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.trident.krishna.mp3alarm.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.trident.krishna.mp3alarm.R.id.nav_history) {
            startActivity(new Intent(this, AlarmHistorymms.class));
        } else if (id == com.trident.krishna.mp3alarm.R.id.nav_my_schedule) {
            startActivity(new Intent(this, MySchedulesmms.class));
        } else if (id == com.trident.krishna.mp3alarm.R.id.nav_add_schedule) {
            startActivity(new Intent(this, AddSchedulemms.class));
        } else if (id == com.trident.krishna.mp3alarm.R.id.nav_allowed_numbers) {
            startActivity(new Intent(this, AllowedNumbersmms.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.trident.krishna.mp3alarm.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS},
                REQUEST_SMS_PERMISSION);
    }

    private void requestDoNotDisturbPermission(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }
}
