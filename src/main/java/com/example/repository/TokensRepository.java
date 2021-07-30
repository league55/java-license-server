package com.example.repository;

import com.example.models.LicenseToken;

import java.util.Optional;

public interface TokensRepository {
    void saveToken(LicenseToken licenseToken);
    Optional<LicenseToken> getToken(String token);
    void resetToken(LicenseToken licenseToken);
}
