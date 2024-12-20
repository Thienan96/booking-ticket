package com.example.booking.base.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.booking.base.service.cache.RedisService;
import com.example.booking.base.service.util.UtilService;

import ch.qos.logback.classic.pattern.Util;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UtilService utilService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String username = null;
        String AT = null;
        String uuid = null;
        try {
            Optional<String> optAT = utilService.getJwtFromRequest(request, AUTHORIZATION, BEARER);
            if (optAT.isPresent()) {
                AT = optAT.get();
                username = jwtUtils.getUsernameFromToken(AT);
                uuid = jwtUtils.getUUID(AT);
                String currentUuid = redisService.get(username);
                if (uuid.equals(currentUuid) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtils.validateToken(AT)) {
                        setSecurityContext(new WebAuthenticationDetailsSource().buildDetails(request), AT);
                    } 
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        chain.doFilter(request, response);
    }

    private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
        try {
            final String username = jwtUtils.getUsernameFromToken(token);
        final List<String> roles = jwtUtils.getRoles(token);
        final UserDetails userDetails = new User(username, "",
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        authentication.setDetails(authDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
