package com.trident.krishna.mp3alarm.Maintodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.trident.krishna.mp3alarm.About.AboutActivity;
import com.trident.krishna.mp3alarm.AppDefault.AppDefaultActivity;
import com.trident.krishna.mp3alarm.R;
import com.trident.krishna.mp3alarm.contact;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppDefaultActivity {
    private InterstitialAd mInterstitialAd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_todo;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return MainFragment.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutMeMenuItem:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;
//            case R.id.switch_themes:
//                if(mTheme == R.style.CustomStyle_DarkTheme){
//                    addThemeToSharedPreferences(LIGHTTHEME);
//                }
//                else{
//                    addThemeToSharedPreferences(DARKTHEME);
//                }
//
////                if(mTheme == R.style.CustomStyle_DarkTheme){
////                    mTheme = R.style.CustomStyle_LightTheme;
////                }
////                else{
////                    mTheme = R.style.CustomStyle_DarkTheme;
////                }
//                this.recreate();
//                return true;
            case R.id.preferences:
                Intent intent = new Intent(this, contact.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
//        super.onBackPressed();
        Intent intent = new Intent(com.trident.krishna.mp3alarm.Maintodo.MainActivity.this, com.trident.krishna.mp3alarm.active.MainActivity.class);
        overridePendingTransition(R.anim.rotate, R.anim.rotate);
        startActivity(intent);
        finish();

    }

}


