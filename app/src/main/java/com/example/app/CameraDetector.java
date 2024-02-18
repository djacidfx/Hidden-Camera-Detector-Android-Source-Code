package com.example.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import vocsy.ads.GoogleAds;

public final class CameraDetector extends AppCompatActivity implements SensorEventListener {
    public static final Companion Companion = new Companion(null);
    private static DecimalFormat DECIMAL_FORMATTER;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private float actVolume;
    private AudioManager audioManager;
    private int counter;
    private boolean loaded;
    private GaugeView mGaugeView1;
    private float maxVolume;
    private boolean plays;
    private SensorManager sensorManager;
    private int soundID;
    private SoundPool soundPool;
    private TextView value;
    private TextView value2;
    private float volume;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view == null) {
            View findViewById = findViewById(i);
            if (findViewById != null) {
                map.put(Integer.valueOf(i), findViewById);
                return findViewById;
            }
            return null;
        }
        return view;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
        Intrinsics.checkNotNullParameter(sensor, "sensor");
    }

    public final boolean getPlays() {
        return this.plays;
    }

    public final void setPlays(boolean z) {
        this.plays = z;
    }

    public final boolean getLoaded() {
        return this.loaded;
    }

    public final void setLoaded(boolean z) {
        this.loaded = z;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_camera_detector);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        View findViewById = findViewById(R.id.value);
        Intrinsics.checkNotNull(findViewById, "null cannot be cast to non-null type android.widget.TextView");
        this.value = (TextView) findViewById;
        View findViewById2 = findViewById(R.id.value2);
        Intrinsics.checkNotNull(findViewById2, "null cannot be cast to non-null type android.widget.TextView");
        this.value2 = (TextView) findViewById2;
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
        decimalFormatSymbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", decimalFormatSymbols);
        Object systemService = getSystemService("sensor");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
        SensorManager sensorManager = (SensorManager) systemService;
        this.sensorManager = sensorManager;
        Intrinsics.checkNotNull(sensorManager);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(2), 3);
        this.mGaugeView1 = (GaugeView) _$_findCachedViewById(R.id.gauge_view1);
    }


    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SensorManager sensorManager = this.sensorManager;
        Intrinsics.checkNotNull(sensorManager);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(2), 3);
        initSoundPool();
    }

    private final void initSoundPool() {
        Object systemService = getSystemService("audio");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.media.AudioManager");
        AudioManager audioManager = (AudioManager) systemService;
        this.audioManager = audioManager;
        Intrinsics.checkNotNull(audioManager);
        this.actVolume = audioManager.getStreamVolume(3);
        AudioManager audioManager2 = this.audioManager;
        Intrinsics.checkNotNull(audioManager2);
        float streamMaxVolume = audioManager2.getStreamMaxVolume(3);
        this.maxVolume = streamMaxVolume;
        this.volume = this.actVolume / streamMaxVolume;
        setVolumeControlStream(3);
        this.counter = 0;
        if (Build.VERSION.SDK_INT >= 21) {
            this.soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build()).setMaxStreams(6).build();
        } else {
            this.soundPool = new SoundPool(6, 3, 0);
        }
        SoundPool soundPool = this.soundPool;
        Intrinsics.checkNotNull(soundPool);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.example.app.CameraDetector$$ExternalSyntheticLambda0
            @Override // android.media.SoundPool.OnLoadCompleteListener
            public final void onLoadComplete(SoundPool soundPool2, int i, int i2) {
                CameraDetector.m193initSoundPool$lambda1(CameraDetector.this, soundPool2, i, i2);
            }
        });
        SoundPool soundPool2 = this.soundPool;
        Intrinsics.checkNotNull(soundPool2);
        this.soundID = soundPool2.load(this, R.raw.beep, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initSoundPool$lambda-1  reason: not valid java name */
    public static final void m193initSoundPool$lambda1(CameraDetector this$0, SoundPool soundPool, int i, int i2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.loaded = true;
    }


    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SensorManager sensorManager = this.sensorManager;
        Intrinsics.checkNotNull(sensorManager);
        sensorManager.unregisterListener(this);
        if (this.plays) {
            SoundPool soundPool = this.soundPool;
            Intrinsics.checkNotNull(soundPool);
            soundPool.stop(this.soundID);
            SoundPool soundPool2 = this.soundPool;
            Intrinsics.checkNotNull(soundPool2);
            this.soundID = soundPool2.load(this, R.raw.beep, this.counter);
            this.plays = false;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        Intrinsics.checkNotNullParameter(sensorEvent, "sensorEvent");
        if (sensorEvent.sensor.getType() == 2) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            double sqrt = Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
            TextView textView = this.value;
            Intrinsics.checkNotNull(textView);
            StringBuilder sb = new StringBuilder();
            DecimalFormat decimalFormat = DECIMAL_FORMATTER;
            Intrinsics.checkNotNull(decimalFormat);
            sb.append(decimalFormat.format(sqrt));
            sb.append(" µTesla");
            textView.setText(sb.toString());
            GaugeView gaugeView = this.mGaugeView1;
            Intrinsics.checkNotNull(gaugeView);
            gaugeView.setTargetValue((float) sqrt);
            double round = Math.round(sqrt * 100.0d) / 100;
            if (round < 80.0d && round > 50.0d) {
                if (this.plays) {
                    SoundPool soundPool = this.soundPool;
                    Intrinsics.checkNotNull(soundPool);
                    soundPool.stop(this.soundID);
                    SoundPool soundPool2 = this.soundPool;
                    Intrinsics.checkNotNull(soundPool2);
                    this.soundID = soundPool2.load(this, R.raw.beep, this.counter);
                    this.plays = false;
                }
                TextView textView2 = this.value2;
                Intrinsics.checkNotNull(textView2);
                textView2.setText("Computer/tv/Mobile Device Detected");
            } else if (round < 120.0d && round > 80.0d) {
                TextView textView3 = this.value2;
                Intrinsics.checkNotNull(textView3);
                textView3.setText("Potential Camera/ Little Speakers Detected");
                if (!this.loaded || this.plays) {
                    return;
                }
                SoundPool soundPool3 = this.soundPool;
                int i = this.soundID;
                float f4 = this.volume;
                Intrinsics.checkNotNull(soundPool3);
                soundPool3.play(i, f4, f4, 1, -1, 1.0f);
                this.counter = this.counter;
                this.plays = true;
            } else if (round >= 160.0d || round <= 120.0d) {
                if (round > 160.0d) {
                    TextView textView4 = this.value2;
                    Intrinsics.checkNotNull(textView4);
                    textView4.setText("High Radiation Detected");
                    return;
                }
                TextView textView5 = this.value2;
                Intrinsics.checkNotNull(textView5);
                textView5.setText("No Camera/ Small Metal Detected");
            } else {
                TextView textView6 = this.value2;
                Intrinsics.checkNotNull(textView6);
                textView6.setText("Potential Camera Detected");
                if (this.plays) {
                    SoundPool soundPool4 = this.soundPool;
                    Intrinsics.checkNotNull(soundPool4);
                    soundPool4.stop(this.soundID);
                    SoundPool soundPool5 = this.soundPool;
                    Intrinsics.checkNotNull(soundPool5);
                    this.soundID = soundPool5.load(this, R.raw.beep, this.counter);
                    this.plays = false;
                }
            }
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final DecimalFormat getDECIMAL_FORMATTER() {
            return CameraDetector.DECIMAL_FORMATTER;
        }

        public final void setDECIMAL_FORMATTER(DecimalFormat decimalFormat) {
            CameraDetector.DECIMAL_FORMATTER = decimalFormat;
        }
    }
}
