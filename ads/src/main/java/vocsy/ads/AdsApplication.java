package vocsy.ads;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdsApplication extends Application {

    public static final String TAG = "AdsApplication";
    public static AdsApplication instance;
    public static AppOpenManager appOpenManager;

    public static synchronized AdsApplication getInstance() {
        AdsApplication application;
        synchronized (AdsApplication.class) {
            application = instance;
        }
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

    }
}
