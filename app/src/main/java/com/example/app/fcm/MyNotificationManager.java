package com.example.app.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.app.BuildConfig;
import com.example.app.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class MyNotificationManager {
    private static MyNotificationManager myNotificationManager;
    private String CHANNEL_ID = "this_is_CameraDetectorChannel_id";
    private Context mContext;

    public MyNotificationManager(Context context) {
        this.mContext = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        MyNotificationManager myNotificationManager2;
        synchronized (MyNotificationManager.class) {
            if (myNotificationManager == null) {
                myNotificationManager = new MyNotificationManager(context);
            }
            myNotificationManager2 = myNotificationManager;
        }
        return myNotificationManager2;
    }

    public void displayNotification(String str, String str2, String str3, String str4, String str5, String str6, int i) {
        PendingIntent activity = PendingIntent.getActivity(this.mContext, 0, new Intent("android.intent.action.VIEW", Uri.parse(str4)), 134217728);
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, (int) R.layout.fcm_notification);
        remoteViews.setTextViewText(R.id.tv_title, str);
        remoteViews.setTextViewText(R.id.fcm_short_dis, str2);
        remoteViews.setTextViewText(R.id.tv_fcm_long_dis, str3);
        if (str3 != null && !str3.isEmpty()) {
            remoteViews.setViewVisibility(R.id.tv_fcm_long_dis, 0);
        } else {
            remoteViews.setViewVisibility(R.id.tv_fcm_long_dis, 8);
        }
        try {
            Bitmap bitmap = Picasso.get().load(str6).get();
            Bitmap bitmap2 = Picasso.get().load(str5).get();
            remoteViews.setImageViewBitmap(R.id.fcm_icon, bitmap);
            remoteViews.setImageViewBitmap(R.id.iv_fcm_feature, bitmap2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder customBigContentView = new NotificationCompat.Builder(this.mContext, this.CHANNEL_ID).setContentTitle(str).setSound(RingtoneManager.getDefaultUri(2)).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(activity).setOnlyAlertOnce(true).setCustomContentView(remoteViews).setCustomBigContentView(remoteViews);
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(this.CHANNEL_ID, "Hidden Camera", 4));
            customBigContentView.setChannelId(this.CHANNEL_ID);
        }
        notificationManager.notify(i, customBigContentView.build());
    }
}
