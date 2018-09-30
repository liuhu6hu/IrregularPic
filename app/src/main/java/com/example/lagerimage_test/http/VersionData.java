package com.example.lagerimage_test.http;

public class VersionData {
//     "versionCode": 200,
//             "versionName": "1.1.0",
//             "context": "1.Version版震撼来袭\n2.链式编程，调用简单\n3.强大的自定义功能，一行代码轻松搞定",
//             "apkUrl": "http://test-1251233192.coscd.myqcloud.com/1_1.apk"

    int versionCode;
    String versionName;
    String info;
    String apkUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getContext() {
        return info;
    }

    public void setContext(String context) {
        this.info = context;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
}
