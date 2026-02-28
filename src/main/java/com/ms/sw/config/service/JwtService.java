package com.ms.sw.config.service;

import com.ms.sw.exception.auth.JwtExpiredException;
import com.ms.sw.exception.auth.JwtInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Service responsible for JWT creation, parsing, and validation.
 *
 * <p>Supports access and refresh tokens, including expiration checks,
 * claim extraction, and signature verification.</p>
 */
@Service
@Slf4j
public class JwtService {

    private final SecretKey secretKey;

    private static final long ACCESS_TOKEN_VALIDITY = 900_000; // 15 minutes in milliseconds

    private static final long REFRESH_TOKEN_VALIDITY = 604_800_000; // 7 days in milliseconds

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the username (subject) from a JWT after full validation.
     *
     * @param token JWT token
     * @return username stored in the token subject
     * @throws JwtExpiredException if the token is expired
     * @throws JwtInvalidException if the token is invalid or malformed
     */
    public String extractUsername(String token) {

        try {
            validateTokenNotExpired(token);
            return extractClaim(token,Claims::getSubject);

        }catch (ExpiredJwtException e){
            log.warn("Attempt to parse token expired exception");
            throw new JwtExpiredException("Token has expired",e);
        }catch (SignatureException e){
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtInvalidException("Invalid Token signature",e);
        }catch (MalformedJwtException e){
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new JwtInvalidException("Malformed token structure",e);
        }catch (Exception e){
            log.error("Unexpected error during token validation: {}", e.getMessage());
            throw new JwtInvalidException("Token validation failed", e);
        }
    }

    /**
     * Validates that a JWT has not expired.
     *
     * @param token JWT token
     * @throws JwtExpiredException if the token has expired
     * @throws JwtInvalidException if validation fails
     */
    public void validateTokenNotExpired(String token) {

        try{
            Date expiration = extractExpiration(token);
            Date now = new Date();

            if(expiration.before(now)){
                long expiredMinutesAgo = (now.getTime() - expiration.getTime()) / 60000;
                log.warn("Token expired {} minutes ago", expiredMinutesAgo);

                throw new JwtExpiredException(
                        String.format("Token expired %d minutes ago",expiredMinutesAgo)
                );
            }
            log.debug("Token expiration validated successfully. Expires at: {}", expiration);

        } catch (JwtExpiredException e) {
            throw e;

        } catch (ExpiredJwtException e) {

            log.warn("Token validation detected expired token via JWT library");
            throw new JwtExpiredException("Token has expired", e);

        } catch (Exception e) {
            log.error("Error validating token expiration: {}", e.getMessage());
            throw new JwtInvalidException("Unable to validate token expiration", e);
        }
    }

    /**
     * Extracts the expiration timestamp from a JWT.
     *
     * @param token JWT token
     * @return token expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT.
     *
     * @param token JWT token
     * @param claimsResolver function selecting the desired claim
     * @param <T> claim type
     * @return extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from token.
     *
     * @param token JWT token
     * @return all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates a short-lived access token.
     *
     * @param username user identifier
     * @return signed JWT access token
     */
    public String generateAccessToken(String username){

        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + ACCESS_TOKEN_VALIDITY);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

        log.info("Generated access token for user: {} (expires in 15 minutes)", username);
        return token;
    }

    /**
     * Generates a long-lived refresh token.
     *
     * @param username user identifier
     * @return signed JWT refresh token
     */
    public String generateRefreshToken(String username) {

        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + REFRESH_TOKEN_VALIDITY);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("type", "refresh")
                .signWith(secretKey)
                .compact();

        log.info("Generated refresh token for user: {} (expires in 7 days)", username);
        return token;
    }

    /**
     * Determines whether the given JWT is a refresh token.
     *
     * @param token JWT token
     * @return {@code true} if the token is marked as a refresh token
     */
    public boolean isRefreshToken(String token){

        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("type", String.class);
            return "refresh".equals(tokenType);

        }catch (Exception e){
            log.error("Error checking if token is refresh token: {}", e.getMessage());
            return false;
        }
    }
}
