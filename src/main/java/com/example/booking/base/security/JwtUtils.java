package com.example.booking.base.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.booking.base.service.cache.impl.RedisServiceImpl;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    public static final String ROLES = "ROLES";
    public static final String ID = "ID";
    public static final String EMAIL = "EMAIL";


    private static final String jwtSecret = "jwtSecretKey";

    // private static Key getSigningKey() {
    // byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    // return Keys.hmacShaKeyFor(keyBytes);
    // }

    static SecretKey getSigningKey() {
        return Jwts.SIG.HS256.key().build();
    }

    private static final SecretKey key = getSigningKey();

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<String> getRoles(String token) {
        return getClaimFromToken(token, claims -> (List) claims.get(ROLES));
    }

    public Long getID(String token) {
        return getClaimFromToken(token, claims -> ((Number) claims.get(ID)).longValue());
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            // throw new Exception("Could not verify JWT token integrity!", e);
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication, Long id) {
        final Map<String, Object> claims = new HashMap<>();
        final UserDetails user = (UserDetails) authentication.getPrincipal();

        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put(ROLES, roles);
        claims.put(ID, id);
        claims.put(EMAIL, authentication.getName());

        return generateToken(claims, user.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 86400L * 1000))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return username != null && !isTokenExpired(token);
    }
}
