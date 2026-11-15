package com.example.logintests.core;

import java.nio.file.Path;
import java.time.Duration;

public record TestConfig(
    String baseUrl,
    String feedbackBaseUrl,
    BrowserEngine browserEngine,
    boolean headless,
    Duration timeout,
    Path artifactDirectory,
    long randomSeed) {
    public static TestConfig load() {
        return new TestConfig(
            value("login.baseUrl", "LOGIN_BASE_URL", "https://practicetestautomation.com/practice-test-login/"),
            value("feedback.baseUrl", "FEEDBACK_BASE_URL", ""),
            BrowserEngine.from(value("login.browser", "LOGIN_BROWSER", "chromium")),
            Boolean.parseBoolean(value("login.headless", "LOGIN_HEADLESS", "true")),
            Duration.ofMillis(parsePositiveLong("login.timeoutMillis", "LOGIN_TIMEOUT_MILLIS", 10_000)),
            Path.of(value("login.artifactDirectory", "LOGIN_ARTIFACT_DIRECTORY", "build/artifacts")),
            parseLong("login.randomSeed", "LOGIN_RANDOM_SEED", 20261112L)
        );
    }

    public String requireFeedbackBaseUrl() {
        if (feedbackBaseUrl == null || feedbackBaseUrl.isBlank()) {
            throw new IllegalStateException("feedback.baseUrl or FEEDBACK_BASE_URL must be configured");
        }
        return feedbackBaseUrl.trim();
    }

    private static String value(String property, String environment, String fallback) {
        String fromProperty = System.getProperty(property);
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty.trim();
        }
        String fromEnvironment = System.getenv(environment);
        return fromEnvironment == null || fromEnvironment.isBlank()
            ? fallback
            : fromEnvironment.trim();
    }

    private static long parsePositiveLong(String property, String environment, long fallback) {
        long value = parseLong(property, environment, fallback);
        if (value <= 0) {
            throw new IllegalArgumentException(property + " must be greater than zero");
        }
        return value;
    }

    private static long parseLong(String property, String environment, long fallback) {
        String value = value(property, environment, Long.toString(fallback));
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid numeric setting " + property + ": " + value, exception);
        }
    }
}
