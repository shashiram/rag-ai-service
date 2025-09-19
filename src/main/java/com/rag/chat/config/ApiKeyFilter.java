package com.rag.chat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

// security/ApiKeyAuthFilter.java
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);
    @Value("${app.api-key}")
    private Set<String> apiKeys;
    @Autowired
    private RateLimiterManager rateLimiterManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-KEY");
        if(apiKey==null){
            filterChain.doFilter(request, response);
            return;
        }

        if (!apiKeys.contains(apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid API Key");
            return;
        }

        if(!rateLimiterManager.tryAcquire(apiKey)){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            String errorMsg="Rate limit exceeded. Please try again later. Api key : "+apiKey;
            logger.error(errorMsg);
            response.getWriter().write(errorMsg);
            return;
        }

        Authentication authentication= UsernamePasswordAuthenticationToken.authenticated(apiKey, Collections.emptyList(),null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
