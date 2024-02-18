package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.GetStart2;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.ExitScreen;
import vocsy.ads.GoogleAds;

public class GetStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(GetStart.this, GetStart2.class));
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetStart.this, ExitScreen.class));
    }
}