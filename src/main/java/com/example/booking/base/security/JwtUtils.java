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

import com.example.booking.base.constant.Const;
import com.example.booking.base.service.cache.impl.RedisServiceImpl;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    public static final String ROLES = "ROLES";
    public static final String UUID = "UUID";
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

    public String getUsernameFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<String> getRoles(String token) throws Exception {
        return getClaimFromToken(token, claims -> (List) claims.get(ROLES));
    }

    public String getUUID(String token) throws Exception {
        return getClaimFromToken(token, claims -> (String) claims.get(UUID));
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) throws Exception {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
    }

    private Boolean isTokenExpired(String token) throws Exception {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public String generateToken(Authentication authentication, UUID uuid, Long expireTime) {
        final UserDetails user = (UserDetails) authentication.getPrincipal();
        final Map<String, Object> claims = generateClaims(authentication, uuid);
        return generateToken(claims, user.getUsername(), expireTime);
    }

    public Map<String, Object> generateClaims(Authentication authentication, UUID uuid) {
        Map<String, Object> claims = new HashMap<>();

        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put(ROLES, roles);
        claims.put(UUID, uuid);
        claims.put(EMAIL, authentication.getName());
        return claims;
    }

    private String generateToken(Map<String, Object> claims, String subject, Long expireTime) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireTime * 1000))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) throws Exception {
        final String username = getUsernameFromToken(token);
        return username != null && !isTokenExpired(token);
    }
}
