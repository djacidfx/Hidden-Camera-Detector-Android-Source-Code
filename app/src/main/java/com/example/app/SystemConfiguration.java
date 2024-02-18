package com.example.app;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

/**
 * SystemConfiguration is for change system icon and backgrounds easily
 * Here i give static methods is for change Configuration by one line of code from anywhere


 * SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_LIGHT);
 * SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_LIGHT);


 * @Author => Urvish Shiroya
 * @Created-At => 10-01-2022
 * <p>
 * $-THANK_YOU-$
 **/

public class SystemConfiguration {

    /**
     * For check what your android version
     **/

    public static boolean isAndroidBetween(int minSdk, int maxSdk) {
        if (Build.VERSION.SDK_INT < minSdk && Build.VERSION.SDK_INT > maxSdk) {
            return false;
        }
        return Build.VERSION.SDK_INT >= minSdk && Build.VERSION.SDK_INT <= maxSdk;
    }

    public static boolean isAndroidLowerThen(int androidVersion) {
        return Build.VERSION.SDK_INT <= androidVersion;
    }

    public static boolean isAndroidHigherThen(int androidVersion) {
        return Build.VERSION.SDK_INT >= androidVersion;
    }


    /**
     * for get StatusBar Height
     **/
    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? activity.getResources().getDimensionPixelSize(resourceId) : (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * activity.getResources().getDisplayMetrics().density);
    }

    /**
     * for get NavigationBar Height
     **/
    public static int getNavigationBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * for get your device screen width
     **/
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * for get your device screen Height
     **/
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * for set transparent StatusBar without hide icons
     **/
    public static void setTransparentStatusBar(Activity activity, IconColor iconColor) {

        boolean isLightIcon = IconColor.ICON_LIGHT.equals(iconColor);

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= 30) {
            if (isLightIcon) {
                activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
            } else {
                activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else {
            changeStatusContrastStyle(activity.getWindow(), isLightIcon);
        }
    }

    /**
     * for set transparent navigationBar without hide icons
     **/
    public static void setTransparentNavigationBar(Activity activity, IconColor iconColor) {

        boolean isLightIcon = IconColor.ICON_LIGHT.equals(iconColor);


        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= 30) {
            if (isLightIcon) {
                activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            } else {
                activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            }
        } else {
            changeNavigationContrastStyle(activity.getWindow(), isLightIcon);
        }
    }

    /**
     * for set StatusBar default white
     **/
    public static void setStatusBarNormal(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setStatusColor(activity, Color.WHITE);
        setStatusLight(activity, true);
        changeStatusContrastStyle(activity.getWindow(), false);
    }


    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setDecorFitsSystemWindows(Activity activity, boolean decorFitsSystemWindows) {
        if (isAndroidHigherThen(Build.VERSION_CODES.R)) {
            activity.getWindow().setDecorFitsSystemWindows(decorFitsSystemWindows);
        }
    }

    /**
     * for set StatusBar Color
     **/
    public static void setStatusBarColor(Activity activity, int colorResId, IconColor iconColor) {
        //if color is dark then set icon white...
        boolean isLightIcon = iconColor.equals(IconColor.ICON_LIGHT);
        setStatusColor(activity, colorResId);
        changeStatusContrastStyle(activity.getWindow(), isLightIcon);
    }

    public static void setStatusLight(Activity activity, boolean isLightStatusBar) {
        if (isAndroidHigherThen(Build.VERSION_CODES.M)) {
            View decor = activity.getWindow().getDecorView();
            if (isLightStatusBar) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
                decor.setSystemUiVisibility(0);
            } else {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.white));
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private static void setStatusColor(Activity activity, int resId) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, resId));
    }

    private static void changeStatusContrastStyle(Window window, boolean lightIcons) {
        View decorView = window.getDecorView();
        if (lightIcons) {
            // Draw light icons on a dark background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // Draw dark icons on a light background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private static boolean isColorDark(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        String firstChar = String.valueOf(hexColor.charAt(1));
        if (firstChar.matches("9") || firstChar.matches("A") || firstChar.matches("B") || firstChar.matches("C") || firstChar.matches("D") || firstChar.matches("E") || firstChar.matches("F")) {
            return true;
        } else {
            return false;
        }
    }


    private static void changeNavigationContrastStyle(Window window, boolean lightIcons) {
        View decorView = window.getDecorView();
        if (lightIcons) {
            // Draw light icons on a dark background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            // Draw dark icons on a light background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    public enum IconColor {
        ICON_LIGHT, ICON_DARK
    }

}
