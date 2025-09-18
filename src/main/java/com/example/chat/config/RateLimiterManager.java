package com.example.chat.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterManager {
    private final RateLimiterConfigProperties rateLimiterConfigProperties;
    private  RateLimiterRegistry registry;
    private final ConcurrentHashMap<String, RateLimiter> cache = new ConcurrentHashMap<>();
    public RateLimiterManager(RateLimiterConfigProperties rateLimiterConfigProperties) {

        this.rateLimiterConfigProperties = rateLimiterConfigProperties;
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(this.rateLimiterConfigProperties.getLimitForPeriod())
                .limitRefreshPeriod(Duration.ofSeconds(this.rateLimiterConfigProperties.getLimitRefreshPeriodSeconds()))
                .timeoutDuration(Duration.ofMillis(this.rateLimiterConfigProperties.getTimeoutDurationMillis()))
                .build();
        this.registry = RateLimiterRegistry.of(config);
    }

    public RateLimiter getForKey(String apiKey) {
        return cache.computeIfAbsent(apiKey, k -> registry.rateLimiter("rl-" + k));
    }

    public boolean tryAcquire(String apiKey) {
        return getForKey(apiKey).acquirePermission();
    }
}
