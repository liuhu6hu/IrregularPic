package com.example.lagerimage_test.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.example.lagerimage_test.MyApplication;

public class PhoneUtils {
    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * Return the unique device id.
     * <p>Must hold
     *
     * @return the unique device id
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId() {
        TelephonyManager tm =
                (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (tm == null) return "";
        String imei = tm.getImei();
            if (!TextUtils.isEmpty(imei)) return imei;
            String meid = tm.getMeid();
            return TextUtils.isEmpty(meid) ? "" : meid;

        }
        return tm != null ? tm.getDeviceId() : "";
    }

    /**
     * Return the IMEI.
     * <p>Must hold
     *
     * @return the IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        TelephonyManager tm =
                (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm != null ? tm.getImei() : "";
        }else {
            return tm != null ? tm.getDeviceId() : "";
        }
    }


}
