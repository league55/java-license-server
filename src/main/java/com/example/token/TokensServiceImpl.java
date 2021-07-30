package com.example.token;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokensServiceImpl implements TokensService {
    private static final Map<String, LicenseToken> tokens = new HashMap<>();

    @Override
    public TokensResponse redeemToken(LicenseToken licenseToken) {
        tokens.put(licenseToken.getToken(), licenseToken);
        return new TokensResponse("OK", 200);
    }

    @Override
    public TokensResponse validateToken(LicenseToken licenseToken) {
        return null;
    }

    @Override
    public void resetToken(LicenseToken licenseToken) {
        tokens.remove(licenseToken.getToken());
    }
}
