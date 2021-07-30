package com.example.token;

public interface TokensService {
    TokensResponse redeemToken(LicenseToken licenseToken);
    TokensResponse validateToken(LicenseToken licenseToken);
    void resetToken(LicenseToken licenseToken);
}
