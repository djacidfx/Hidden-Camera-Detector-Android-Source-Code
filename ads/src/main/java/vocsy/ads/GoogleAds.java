package vocsy.ads;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class GoogleAds {

    private static final String TAG = "Google Ads => ";
    private static GoogleAds instance;
    private NativeAd nativeAd00;
    private CustomAdsListener listener;
    private Dialog dialog;

    private GoogleAds() {

    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet


            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    public static GoogleAds getInstance() {
        if (instance == null) {
            synchronized (GoogleAds.class) {
                if (instance == null)
                    instance = new GoogleAds();
            }
        }
        // Return the instance
        return instance;
    }

    public boolean admobBanner(final Context context, final View customView) {
        boolean returnStatement = false;

        if (checkConnection(context) && AdsHandler.isAdsOn()) {
            returnStatement = true;

            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(AdsHandler.bannerId);
            AdRequest adre = new AdRequest.Builder().build();
            mAdView.loadAd(adre);

            if (customView instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            } else if (customView instanceof RelativeLayout) {
                RelativeLayout layout = (RelativeLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            } else if (customView instanceof FrameLayout) {
                FrameLayout layout = (FrameLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            }


            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // TODO Auto-generated method stub
                    super.onAdLoaded();
                    customView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    customView.setVisibility(View.GONE);
                }
            });
        }

        return returnStatement;
    }

    public boolean admobBanner90(final Context context, final View customView) {

        boolean returnStatement = false;

        if (checkConnection(context) && AdsHandler.isAdsOn()) {

            returnStatement = true;

            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(AdsHandler.bannerId);
            AdRequest adre = new AdRequest.Builder().build();
            mAdView.loadAd(adre);
            if (customView instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            } else if (customView instanceof RelativeLayout) {
                RelativeLayout layout = (RelativeLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            } else if (customView instanceof FrameLayout) {
                FrameLayout layout = (FrameLayout) customView;
                layout.removeAllViews();
                layout.addView(mAdView);
            }

            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    // TODO Auto-generated method stub
                    customView.setVisibility(View.VISIBLE);
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    customView.setVisibility(View.GONE);
                }
            });
        }
        return returnStatement;
    }


    public void showCounterInterstitialAd(Activity activity, CustomAdsListener customAdsListener) {
        this.listener = customAdsListener;
        if (AdsHandler.sharedPreferences == null) {
            AdsHandler.getInstance(activity);
        }

        if (checkConnection(activity) && AdsHandler.isAdsOn()) {

            showLoading(activity, false);

            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(activity, AdsHandler.interstitialId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            listener.onFinish();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            listener.onFinish();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {

                        }
                    });
                    hideLoading();
                    interstitialAd.show(activity);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Toast.makeText(activity, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    hideLoading();
                    listener.onFinish();
                }
            });
        } else {
            listener.onFinish();
        }

    }

    public void showRewardedAd(Activity activity, CustomAdsListener customAdsListener) {

        this.listener = customAdsListener;
        if (AdsHandler.sharedPreferences == null) {
            AdsHandler.getInstance(activity);
        }

        if (checkConnection(activity) && AdsHandler.isAdsOn()) {

            showLoading(activity, false);

            AdRequest adRequest = new AdRequest.Builder().build();

            RewardedAd.load(activity, AdsHandler.rewardedId,
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            hideLoading();
                            if (listener != null) {
                                listener.onFinish();
                            }

                            Log.e(TAG, loadAdError.getMessage());
//                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdShowedFullScreenContent() {

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    hideLoading();
                                    if (listener != null) {
                                        listener.onFinish();
                                    }
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    hideLoading();
                                    if (listener != null) {
                                        listener.onFinish();
                                    }
                                }


                            });

                            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    hideLoading();
//                                    if (listener != null) {
//                                        listener.onFinish();
//                                    }
                                }
                            });

                            hideLoading();
                        }
                    });
        }

    }

    public boolean addNativeView(Context mContext, View customView) {

        boolean returnStatement = false;

        if (checkConnection(mContext) && AdsHandler.isAdsOn()) {

            returnStatement = true;

            AdLoader.Builder builder = new AdLoader.Builder(mContext, AdsHandler.nativeId);

            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {

                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            if (nativeAd00 != null) {
                                nativeAd00.destroy();
                            }
                            nativeAd00 = nativeAd;
                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            NativeAdView mAdView =
                                    (NativeAdView) inflater.inflate(R.layout.small_ad_unified, null);
                            populateNativeAdView(nativeAd, mAdView);
                            if (customView instanceof LinearLayout) {
                                LinearLayout layout = (LinearLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            } else if (customView instanceof RelativeLayout) {
                                RelativeLayout layout = (RelativeLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            } else if (customView instanceof FrameLayout) {
                                FrameLayout layout = (FrameLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            }
                        }
                    });


            AdLoader adLoader =
                    builder
                            .withAdListener(
                                    new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(LoadAdError loadAdError) {

                                        }
                                    })
                            .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }

        return returnStatement;
    }

    public boolean addBigNativeView(Context mContext, View customView) {

        boolean returnStatement = false;

        if (checkConnection(mContext) && AdsHandler.isAdsOn()) {

            returnStatement = true;

            AdLoader.Builder builder = new AdLoader.Builder(mContext, AdsHandler.nativeId);

            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {

                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            if (nativeAd00 != null) {
                                nativeAd00.destroy();
                            }
                            nativeAd00 = nativeAd;
                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            NativeAdView mAdView =
                                    (NativeAdView) inflater.inflate(R.layout.big_ad_unified, null);
                            populateNativeAdView(nativeAd, mAdView);
                            if (customView instanceof LinearLayout) {
                                LinearLayout layout = (LinearLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            } else if (customView instanceof RelativeLayout) {
                                RelativeLayout layout = (RelativeLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            } else if (customView instanceof FrameLayout) {
                                FrameLayout layout = (FrameLayout) customView;
                                layout.removeAllViews();
                                layout.addView(mAdView);
                            }
                        }
                    });


            AdLoader adLoader =
                    builder
                            .withAdListener(
                                    new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(LoadAdError loadAdError) {

                                        }
                                    })
                            .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }

        return returnStatement;
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

    }


    public void showLoading(Activity activity, boolean cancelable) {

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(cancelable);

        if (!dialog.isShowing() && !activity.isFinishing()) {
            dialog.show();
        }
    }

    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

}

