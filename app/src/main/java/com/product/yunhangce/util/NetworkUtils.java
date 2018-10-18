package com.product.yunhangce.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

import com.product.yunhangce.MyApplication;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class NetworkUtils {
    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * Open the settings of wireless.
     */
    public static void openWirelessSettings() {
        MyApplication.getInstance().startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }


    /**
     * Return whether network is connected.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager manager =
                (ConnectivityManager)MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return null;
        return manager.getActiveNetworkInfo();
    }
}
