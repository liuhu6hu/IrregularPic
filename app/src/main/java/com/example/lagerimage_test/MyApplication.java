package com.example.lagerimage_test;

import android.app.Application;

import com.example.lagerimage_test.http.AppInfo;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private static AppInfo mAppInfo;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static AppInfo getCommonDate() {
        return mAppInfo;
    }

    public static void setCommonDate(AppInfo appInfo) {
        mAppInfo= appInfo;

    }
}
