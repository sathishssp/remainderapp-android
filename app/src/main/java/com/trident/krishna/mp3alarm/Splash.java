package com.trident.krishna.mp3alarm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.trident.krishna.mp3alarm.Utility.BackgroundService;

public class Splash extends AppCompatActivity {
    TextView textslogan,textpowered;
    ImageView splash;
    private static int TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textslogan = (TextView)findViewById(R.id.textslogan);
        textpowered = (TextView)findViewById(R.id.textslogan);
        splash = (ImageView)findViewById(R.id.splash);

        MobileAds.initialize(this,"ca-app-pub-6095264288861112~2425095397");

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        splash.startAnimation(anim);
        textslogan.startAnimation(anim);
        textpowered.startAnimation(anim);



        startService(new Intent(this, BackgroundService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, com.trident.krishna.mp3alarm.active.MainActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotate);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
    }

