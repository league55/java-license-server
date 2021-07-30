package com.example.services;

import com.example.models.LicenseToken;
import com.example.models.TokensResponse;
import com.example.repository.TokensRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class TokensServiceImpl implements TokensService {
    private static final String APP_ID = "appId";
    private static final String OK = "OK";
    Logger logger = LoggerFactory.getLogger(TokensServiceImpl.class);

    @Value("token-secret")
    private String secret;
    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public TokensResponse redeemToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if(!jwtBody.isPresent()) {
            return new TokensResponse(getSignedResponse("Untrusted token"));
        }
        Claims claims = jwtBody.get();
        String token = claims.getSubject();
        String appId = claims.get(APP_ID, String.class);

        Optional<LicenseToken> prevToken = tokensRepository.getToken(token);
        if(prevToken.isPresent()) {
            logger.info("Overriding access token from app '{}' to app '{}'", prevToken.get().getAppID(), appId);
        }
        tokensRepository.saveToken(new LicenseToken(token, appId));

        return new TokensResponse(getSignedResponse(OK));
    }

    @Override
    public TokensResponse validateToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if(!jwtBody.isPresent()) {
            return new TokensResponse(getSignedResponse("Untrusted token"));
        }
        Claims claims = jwtBody.get();
        String token = claims.getSubject();
        String appId = claims.get(APP_ID, String.class);

        Optional<LicenseToken> prevToken = tokensRepository.getToken(token);
        if(prevToken.isPresent() && prevToken.get().getAppID().equals(appId)) {
            logger.info("Overriding access token from app '{}' to app '{}'", prevToken.get().getAppID(), appId);
            return new TokensResponse(getSignedResponse(OK));
        }

        return new TokensResponse(getSignedResponse("Other app uses the token"));
    }

    @Override
    public void resetToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if(jwtBody.isPresent()) {
            Claims claims = jwtBody.get();
            String token = claims.getSubject();
            Optional<LicenseToken> licenseToken = tokensRepository.getToken(token);
            licenseToken.ifPresent(value -> tokensRepository.resetToken(value));
        }
    }

    private String getSignedResponse(String subject) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).signWith(key).compact();
    }

    private Optional<Claims> getBody(String jws) {
        try {
            return Optional.of(Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(jws).getBody());
        } catch (JwtException e) {
            logger.warn("Untrusted JWT token '{}'", e.getMessage());
        }
        return Optional.empty();
    }
}
