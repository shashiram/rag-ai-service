package com.rag.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterConfigProperties {
    private int limitForPeriod = 100; // default value
    private int limitRefreshPeriodSeconds = 60; // default value
    private int timeoutDurationMillis = 0; // default value

    // Getters and setters
    public int getLimitForPeriod() {
        return limitForPeriod;
    }

    public void setLimitForPeriod(int limitForPeriod) {
        this.limitForPeriod = limitForPeriod;
    }

    public int getLimitRefreshPeriodSeconds() {
        return limitRefreshPeriodSeconds;
    }

    public void setLimitRefreshPeriodSeconds(int limitRefreshPeriodSeconds) {
        this.limitRefreshPeriodSeconds = limitRefreshPeriodSeconds;
    }

    public int getTimeoutDurationMillis() {
        return timeoutDurationMillis;
    }

    public void setTimeoutDurationMillis(int timeoutDurationMillis) {
        this.timeoutDurationMillis = timeoutDurationMillis;
    }
}
