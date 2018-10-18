package com.product.yunhangce.http;

public class VersionResponse {
    private int code;
    private String message;
    private VersionData data;
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

    public VersionData getData() {
        return data;
    }

    public void setData(VersionData data) {
        this.data = data;
    }
}
