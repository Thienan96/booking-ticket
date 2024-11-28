package com.example.booking.base.service.util;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UtilService {
    public UUID generateUUID() {
        return UUID.randomUUID();
    }

    public Optional<String> getJwtFromRequest(HttpServletRequest request, String tokenType, String BEARER) {
        String bearerToken = request.getHeader(tokenType);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return Optional.of(bearerToken.substring(BEARER.length()));
        }
        return Optional.empty();
    }
}
