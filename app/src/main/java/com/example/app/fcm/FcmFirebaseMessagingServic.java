package com.example.app.fcm;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.concurrent.atomic.AtomicInteger;


public class FcmFirebaseMessagingServic extends FirebaseMessagingService {
    String ICON_KEY = "icon";
    String APP_TITLE_KEY = "title";
    String SHORT_DESC_KEY = "short_desc";
    String LONG_DESC_KEY = "long_desc";
    String APP_FEATURE_KEY = "feature";
    String APP_URL_KEY = "app_url";
    String IS_PREMIUM = "is_premium";
    private AtomicInteger seed = new AtomicInteger();

    @Override 
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MyFCMToken", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("MyFCMToken", "Message data payload: " + remoteMessage.getData());
        } else {
            Log.d("MyFCMToken", "Message Notification Body: " + remoteMessage.getData());
        }
        if (remoteMessage.getData() != null) {
            Log.d("MyFCMToken", remoteMessage.getData().get("title"));
            Log.d("MyFCMToken", remoteMessage.getData().get("short_desc"));
            String str = remoteMessage.getData().get(this.ICON_KEY);
            String str2 = remoteMessage.getData().get(this.APP_TITLE_KEY);
            String str3 = remoteMessage.getData().get(this.SHORT_DESC_KEY);
            String str4 = remoteMessage.getData().get(this.LONG_DESC_KEY);
            String str5 = remoteMessage.getData().get(this.APP_FEATURE_KEY);
            String str6 = remoteMessage.getData().get(this.APP_URL_KEY);
            if (str == null || str2 == null || str3 == null || str5 == null || str6 == null) {
                return;
            }
            try {
                str6.substring(47);
                if (TinyDB.getInstance(this).getBoolean(this.IS_PREMIUM)) {
                    return;
                }
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(str2, str3, str4, str6, str5, str, getNotificationID());
                return;
            } catch (Exception unused) {
                return;
            }
        }
        Log.d("MyFCMToken", "Message Notification Body: " + remoteMessage.getData());
    }

    @Override 
    public void onNewToken(String str) {
        super.onNewToken(str);
        Log.d("MyFCMToken", "onNewToken: " + str);
    }

    private int getNotificationID() {
        return this.seed.incrementAndGet();
    }
}
