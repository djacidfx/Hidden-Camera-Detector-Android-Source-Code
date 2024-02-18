package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.GetStart2;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public final class MainActivity2 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main2);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
        GoogleAds.getInstance().addNativeView(this, (LinearLayout) findViewById(R.id.nativeLay));
        findViewById(R.id.howToUse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(MainActivity2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                    }
                });

            }
        });
        findViewById(R.id.camdetor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, CameraDetector.class));
            }
        });
        findViewById(R.id.infradcamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity2.m207onCreate$lambda4(MainActivity2.this, view);
            }
        });
    }

    public static void m207onCreate$lambda4(MainActivity2 this$0, View view) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                this$0.startActivity(new Intent(this$0, InfraredCamera.class));
            } else if (this$0.checkSelfPermission("android.permission.CAMERA") != 0) {
                this$0.requestPermissions(new String[]{"android.permission.CAMERA"}, 99);
            } else {
                this$0.startActivity(new Intent(this$0, InfraredCamera.class));
            }
        } catch (Exception unused) {
        }
    }

    public static void m208onCreate$lambda5(MainActivity2 this$0, View view) {
        try {
            this$0.showPopup(this$0.findViewById(R.id.imageView));
        } catch (Exception unused) {
        }
    }

    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.example.app.MainActivity2$$ExternalSyntheticLambda6
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                boolean m210showPopup$lambda6;
                m210showPopup$lambda6 = MainActivity2.m210showPopup$lambda6(MainActivity2.this, menuItem);
                return m210showPopup$lambda6;
            }
        });
        popupMenu.show();
    }

    public static boolean m210showPopup$lambda6(MainActivity2 this$0, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.priitem2) {
            this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://docs.google.com/document/d/1qi8D2Ne_31kcrIH4ki--TBCHWkVHHRVKLStIUuZN1EM/edit")));
            return true;
        } else if (itemId == R.id.rateitem1) {
            this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + this$0.getPackageName())));
            return true;
        } else if (itemId != R.id.shareitem3) {
            return true;
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + this$0.getPackageName());
            intent.setType("text/plain");
            this$0.startActivity(intent);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 99 || iArr.length <= 0) {
            return;
        }
        if (iArr[0] == 0) {
            startActivity(new Intent(this, InfraredCamera.class));
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CAMERA")) {
            Toast.makeText(this, "camera permission denied", 0).show();
        } else {
            MainActivity2 mainActivity2 = this;
            Toast.makeText(mainActivity2, "camera permission denied Permanently", 0).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity2);
            builder.setTitle("Permission");
            builder.setMessage("You have previously declined this permission.\nYou must approve this Camera permission in Permissions  in the app settings on your device.");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    MainActivity2.this.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:com.hiddencamera.infrared.cameradetector")));
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GetStart2.class));
    }
}