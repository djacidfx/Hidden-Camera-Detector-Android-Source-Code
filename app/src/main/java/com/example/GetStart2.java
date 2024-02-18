package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.app.GetStart;
import com.example.app.MainActivity2;
import com.example.app.R;
import com.example.app.SystemConfiguration;

import vocsy.ads.AppUtil;
import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public class GetStart2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start2);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
        GoogleAds.getInstance().addNativeView(this, (LinearLayout) findViewById(R.id.nativeLay));
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStart2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(GetStart2.this, MainActivity2.class));
                    }
                });
            }
        });

        findViewById(R.id.rate).setOnClickListener(v -> {

            AppUtil.rateApp(GetStart2.this);


        });
        findViewById(R.id.privacy).setOnClickListener(v -> {

            AppUtil.privacyPolicy(GetStart2.this, getString(R.string.privacy_policy));


        });
        findViewById(R.id.share).setOnClickListener(v -> {

            AppUtil.shareApp(GetStart2.this);


        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetStart2.this, GetStart.class));
    }
}