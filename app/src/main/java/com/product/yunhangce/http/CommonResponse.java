package com.product.yunhangce.http;

public class CommonResponse {
    private int code;
    private String message;
    private AppInfo data;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppInfo getData() {
        return data;
    }

    public void setData(AppInfo data) {
        this.data = data;
    }
}
