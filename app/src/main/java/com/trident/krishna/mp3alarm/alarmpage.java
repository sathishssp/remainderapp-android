package com.trident.krishna.mp3alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.trident.krishna.mp3alarm.Utility.Utils;

public class alarmpage extends AppCompatActivity {

    TextView general,alarmone,alarmtwelve,alarmtwentyfour;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpage);
        Utils.selectedAlarmType="";
        general = (TextView)findViewById(R.id.generalalarm);
        alarmone = (TextView)findViewById(R.id.alaramone);
        alarmtwelve = (TextView)findViewById(R.id.alarmtwelve);
        alarmtwentyfour = (TextView)findViewById(R.id.alarmtwentyfour);

       MobileAds.initialize(this,"ca-app-pub-1557256197043076~4808962026");

       mAdView=(AdView)findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder().build();
       mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1557256197043076/3296569802");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.show();

     /*   Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            new FancyShowCaseView.Builder(this)
                    .title("This page is to alarm for 1 hr,12hr,24hr")
                    .showOnce("id0")
                    .build()
                    .show();
        }*/

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectedAlarmType="";
                Intent intent = new Intent(alarmpage.this,com.trident.krishna.mp3alarm.view.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectedAlarmType="1";
                Intent intent = new Intent(alarmpage.this,com.trident.krishna.mp3alarm.viewone.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmtwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectedAlarmType="12";
                Intent intent = new Intent(alarmpage.this,com.trident.krishna.mp3alarm.viewtwelve.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });

        alarmtwentyfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectedAlarmType="24";
                Intent intent = new Intent(alarmpage.this,com.trident.krishna.mp3alarm.viewtwenty.activity.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(intent);
                finish();
            }
        });
    }



    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(alarmpage.this, com.trident.krishna.mp3alarm.active.MainActivity.class);
        overridePendingTransition(R.anim.rotate, R.anim.rotate);
        startActivity(intent);
        finish();

    }
}
