package vocsy.ads;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetSmartAdmob extends AsyncTask<Void, Void, Boolean> {

    private static Activity activity;
    private SmartListener listener;
    private String[] adsId;

    public GetSmartAdmob(Activity activity, String[] adsId, SmartListener listener) {
        GetSmartAdmob.activity = activity;
        this.adsId = adsId;
        this.listener = listener;
        AdsHandler.getInstance(activity);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            AdsHandler.bannerId = adsId[0];
            AdsHandler.nativeId = adsId[1];
            AdsHandler.interstitialId = adsId[2];
            AdsHandler.openAds = adsId[3];
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.getMessage();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {

        if (AdsHandler.openAds != null && !AdsHandler.openAds.isEmpty() && !AdsHandler.openAds.equals("0")) {
            AdsApplication.appOpenManager = new AppOpenManager(AdsApplication.getInstance());
        }

        listener.onFinish(success);
        super.onPostExecute(success);
    }
}
