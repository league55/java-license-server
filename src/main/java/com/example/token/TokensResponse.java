package com.example.token;

public class TokensResponse {
    private final String message;
    private final int code;

    public TokensResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
