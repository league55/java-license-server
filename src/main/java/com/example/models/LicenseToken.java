package com.example.models;

public class LicenseToken {
    private String token;
    private String appID;

    public LicenseToken(String token, String appID) {
        this.token = token;
        this.appID = appID;
    }

    public LicenseToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }
}
