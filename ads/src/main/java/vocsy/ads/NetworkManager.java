package vocsy.ads;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

public class NetworkManager {

    private static Activity activity;
    private static OnMonitorListener listener;

    private static NetworkRequest networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build();

    private static ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);

            if (listener != null) {
                listener.onConnectivityChanged(true);
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            if (listener != null) {
                listener.onConnectivityChanged(false);
            }
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean isWifiNetwork = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        }
    };

    public interface OnMonitorListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public static void Monitoring(Activity activity, OnMonitorListener listener) {
        NetworkManager.activity = activity;
        NetworkManager.listener = listener;

        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager) activity.getSystemService(ConnectivityManager.class);
        }
        if (connectivityManager != null) {
            connectivityManager.requestNetwork(networkRequest, networkCallback);
        }
    }

}
