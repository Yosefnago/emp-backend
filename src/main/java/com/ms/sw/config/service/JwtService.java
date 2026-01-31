package com.ms.sw.config.service;

import com.ms.sw.exception.auth.jwt.JwtExpiredException;
import com.ms.sw.exception.auth.jwt.JwtInvalidException;
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
     * Extracts username from token with full validation.
     *
     * @param token JWT token
     * @return username (subject) from the token
     * @throws JwtExpiredException if token has expired
     * @throws JwtInvalidException if token is invalid (signature, malformed, erc.)
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
     * Validates that the token has not expired.
     *
     * @param token JWT token to validate
     * @throws JwtExpiredException if token has expired
     * @throws JwtInvalidException if token cannot be parsed
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
     * Extracts expiration date from token.
     *
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    /**
     * Generic method to extract any claim from token.
     *
     * @param token JWT token
     * @param claimsResolver function to extract specific claim
     * @param <T> type of claim to extract
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
     * Generates an access token for the given username.
     *
     * @param username user's username
     * @return JWT access token
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
     * Generates a refresh token for the given username.
     *
     * @param username user's username
     * @return JWT refresh token
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
     * Validates that a token is specifically a refresh token.
     *
     * @param token JWT token to validate
     * @return true if token is a valid refresh token
     * @throws JwtInvalidException if token is not a refresh token
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
