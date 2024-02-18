package com.example.app;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public final class InfraredCamera extends AppCompatActivity implements SurfaceHolder.Callback {
    private Animation animBlink;
    private Camera camera;
    private LayoutInflater controlInflater;
    private boolean previewing;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_infrared_camera);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        getWindow().setFormat(0);
        View findViewById = findViewById(R.id.camerapreview);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.view.SurfaceView");
        SurfaceView surfaceView = (SurfaceView) findViewById;
        this.surfaceView = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        this.surfaceHolder = holder;
        Intrinsics.checkNotNull(holder);
        holder.addCallback(this);
        SurfaceHolder surfaceHolder = this.surfaceHolder;
        Intrinsics.checkNotNull(surfaceHolder);
        surfaceHolder.setType(3);
        LayoutInflater from = LayoutInflater.from(getBaseContext());
        this.controlInflater = from;
        Intrinsics.checkNotNull(from);
        View inflate = from.inflate(R.layout.infra_verlay, (ViewGroup) null);
        Intrinsics.checkNotNullExpressionValue(inflate, "from.inflate(R.layout.in…rlay, null as ViewGroup?)");
        this.animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        View findViewById2 = inflate.findViewById(R.id.imageView);
        Objects.requireNonNull(findViewById2, "null cannot be cast to non-null type android.widget.ImageView");
        ((ImageView) findViewById2).setAnimation(this.animBlink);
        addContentView(inflate, new ViewGroup.LayoutParams(-1, -1));
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            Camera open = Camera.open();
            this.camera = open;
            if (open != null) {
                open.setDisplayOrientation(90);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.previewing) {
            Camera camera = this.camera;
            Intrinsics.checkNotNull(camera);
            camera.stopPreview();
            this.previewing = false;
        }
        Camera camera2 = this.camera;
        if (camera2 != null) {
            try {
                camera2.setPreviewDisplay(surfaceHolder);
                Camera camera3 = this.camera;
                Intrinsics.checkNotNull(camera3);
                camera3.startPreview();
                this.previewing = true;
                Camera camera4 = this.camera;
                Intrinsics.checkNotNull(camera4);
                Camera.Parameters parameters = camera4.getParameters();
                List<String> supportedFocusModes = parameters.getSupportedFocusModes();
                Intrinsics.checkNotNullExpressionValue(supportedFocusModes, "parameters.supportedFocusModes");
                if (CollectionsKt.contains(supportedFocusModes, "continuous-picture")) {
                    parameters.setFocusMode("continuous-picture");
                } else if (CollectionsKt.contains(supportedFocusModes, "auto")) {
                    parameters.setFocusMode("auto");
                }
                Camera camera5 = this.camera;
                Intrinsics.checkNotNull(camera5);
                camera5.setParameters(parameters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Camera camera = this.camera;
        if (camera != null) {
            camera.stopPreview();
            Camera camera2 = this.camera;
            Intrinsics.checkNotNull(camera2);
            camera2.release();
            this.camera = null;
            this.previewing = false;
        }
    }
}
