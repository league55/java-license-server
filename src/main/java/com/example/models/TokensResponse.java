package com.example.models;

public class TokensResponse {
    private final String jws;

    public TokensResponse(String jws) {
        this.jws = jws;
    }

    public String getJws() {
        return jws;
    }
}
