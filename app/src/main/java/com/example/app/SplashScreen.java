package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

// import com.google.firebase.messaging.FirebaseMessaging;

import vocsy.ads.AdsHandler;
import vocsy.ads.GetSmartAdmob;

public final class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);

        String[] adsUrls = new String[]{getString(R.string.bnr_admob)// 1st Banner Ads Id
                , getString(R.string.native_admob)// 2st Native Ads Id
                , getString(R.string.int_admob)// 3st interstitial Ads Id
                , getString(R.string.app_open_admob)// 4st App-Open Ads Id
                , getString(R.string.video_admob)// 5st Rewarded Ads Id
        };


        new GetSmartAdmob(this, adsUrls, (success) -> {
            // admob init Success
        }).execute();

        AdsHandler.setAdsOn(true);
//        FirebaseMessaging.getInstance().subscribeToTopic(getPackageName());
        getWindow().setFlags(1024, 1024);
        new Handler(getMainLooper()).postDelayed(() -> startActivity(new Intent(SplashScreen.this, GetStart.class)), 2500L);
    }
}
