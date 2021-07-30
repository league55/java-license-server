package com.example.repository;

import com.example.models.LicenseToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokensRepositoryImpl implements TokensRepository {
    private static final Map<String, LicenseToken> tokens = new HashMap<>();

    @Override
    public void saveToken(LicenseToken licenseToken) {
        tokens.put(licenseToken.getToken(), licenseToken);
    }

    @Override
    public Optional<LicenseToken> getToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public void resetToken(LicenseToken licenseToken) {
        tokens.remove(licenseToken.getToken());
    }
}
