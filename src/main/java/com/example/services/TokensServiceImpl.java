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
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class TokensServiceImpl implements TokensService {
    private static final String SESSION_ID = "sessionId";
    private static final String OK = "OK";
    Logger logger = LoggerFactory.getLogger(TokensServiceImpl.class);

    @Value("${token-secret}")
    private String secret;
    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public TokensResponse redeemToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if (!jwtBody.isPresent()) {
            return new TokensResponse(getSignedResponse("Untrusted token"));
        }
        Claims claims = jwtBody.get();
        String token = claims.getSubject();
        String sessionId = claims.get(SESSION_ID, String.class);

        Optional<LicenseToken> prevToken = tokensRepository.getToken(token);
        if (prevToken.isPresent()) {
            if (!prevToken.get().getSessionId().equals(sessionId)) {
                logger.info("Overriding access token from session '{}' to session '{}'", prevToken.get().getSessionId(), sessionId);
                tokensRepository.saveToken(new LicenseToken(token, sessionId));
                return new TokensResponse(getSignedResponse(OK));
            } else {
                logger.info("Re-login '{}'", prevToken.get().getSessionId());
                return new TokensResponse(getSignedResponse(OK));
            }
        } else {
            return new TokensResponse(getSignedResponse("Token is not valid"));
        }
    }

    @Override
    public TokensResponse validateToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if (!jwtBody.isPresent()) {
            return new TokensResponse(getSignedResponse("Untrusted token"));
        }
        Claims claims = jwtBody.get();
        String token = claims.getSubject();
        String sessionId = claims.get(SESSION_ID, String.class);

        Optional<LicenseToken> prevToken = tokensRepository.getToken(token);
        if (prevToken.isPresent() && prevToken.get().getSessionId().equals(sessionId)) {
            logger.info("Overriding access token from app '{}' to app '{}'", prevToken.get().getSessionId(), sessionId);
            return new TokensResponse(getSignedResponse(OK));
        }

        return new TokensResponse(getSignedResponse("Another application uses the token"));
    }

    @Override
    public void resetToken(String jws) {
        Optional<Claims> jwtBody = getBody(jws);
        if (jwtBody.isPresent()) {
            Claims claims = jwtBody.get();
            String token = claims.getSubject();
            Optional<LicenseToken> licenseToken = tokensRepository.getToken(token);
            licenseToken.ifPresent(value -> tokensRepository.resetToken(value));
        }
    }

    private String getSignedResponse(String subject) {
        logger.info("Signing response with subject: '{}'", subject);
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).signWith(key).compact();
    }

    private Optional<Claims> getBody(String jws) {
        try {
            String encodedKey = Base64.getEncoder().encodeToString(secret.getBytes());
            Claims body = Jwts.parserBuilder().setSigningKey(encodedKey).build().parseClaimsJws(jws).getBody();
            return Optional.of(body);
        } catch (JwtException e) {
            logger.warn("Untrusted JWT token '{}'", e.getMessage());
        }
        return Optional.empty();
    }
}
