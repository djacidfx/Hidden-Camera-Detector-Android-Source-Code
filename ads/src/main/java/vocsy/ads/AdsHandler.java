package vocsy.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;

public class AdsHandler {
    /**
     * always remember to remove this test id's, after complete app.
     */
    public static AdsHandler instance;
    public static String bannerId = "";
    public static String nativeId = "";
    public static String interstitialId = "";
    public static String rewardedId = "";
    public static String openAds = "";

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    public AdsHandler() {
    }

    public static byte[] getByte (Context context, int i) throws IOException {
         return  IOUtils.toByteArray(context.getResources().openRawResource(i));
    }

    public static void setAdsOn(boolean switchAds) {
        editor.putBoolean("ads", switchAds);
        editor.apply();
    }

    public static boolean isAdsOn() {
        return sharedPreferences != null && sharedPreferences.getBoolean("ads", true);
    }

    @SuppressLint("CommitPrefEdits")
    public static synchronized AdsHandler getInstance(Activity activity) {
        AdsHandler.activity = activity;
        sharedPreferences = activity.getSharedPreferences("AdmobPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (instance == null) {
            instance = new AdsHandler();
        }

        return instance;
    }
}
