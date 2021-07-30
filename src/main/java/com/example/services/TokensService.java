package com.example.services;

import com.example.models.TokensResponse;

public interface TokensService {
    TokensResponse redeemToken(String jws);
    TokensResponse validateToken(String jws);
    void resetToken(String jws);
}
