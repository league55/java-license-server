package com.example.models;

public class LicenseToken {
    private String token;
    private String sessionId;

    public LicenseToken(String token, String sessionId) {
        this.token = token;
        this.sessionId = sessionId;
    }

    public LicenseToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
