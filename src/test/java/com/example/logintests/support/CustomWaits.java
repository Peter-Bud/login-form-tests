package com.example.logintests.support;

import dev.failsafe.RetryPolicy;

import java.time.Duration;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import static dev.failsafe.Failsafe.with;

public final class CustomWaits {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofMillis(100);
    private static final int MAX_ATTEMPTS = Integer.MAX_VALUE;
    private static final String TIMEOUT_MUST_NOT_BE_NEGATIVE = "timeout must not be negative";
    private static final String POLL_INTERVAL_MUST_BE_POSITIVE = "pollInterval must be greater than zero";

    private CustomWaits() {}

    public static boolean waitForCondition(BooleanSupplier condition) {
        return waitForCondition(condition, DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    public static boolean waitForCondition(BooleanSupplier condition, Duration timeout) {
        return waitForCondition(condition, timeout, DEFAULT_POLL_INTERVAL);
    }

    public static boolean waitForCondition(BooleanSupplier condition, Duration timeout, Duration pollInterval) {
        Objects.requireNonNull(condition);
        requireValidTimeout(timeout);
        requireValidPollInterval(pollInterval);

        RetryPolicy<Boolean> retryPolicy = RetryPolicy.<Boolean>builder()
            .handleResult(false)
            .withMaxAttempts(MAX_ATTEMPTS)
            .withMaxDuration(timeout)
            .withDelay(pollInterval)
            .build();

        return with(retryPolicy).get(condition::getAsBoolean);
    }

    private static void requireValidTimeout(Duration timeout) {
        Objects.requireNonNull(timeout);
        if (timeout.isNegative()) {
            throw new IllegalArgumentException(TIMEOUT_MUST_NOT_BE_NEGATIVE);
        }
    }

    private static void requireValidPollInterval(Duration pollInterval) {
        Objects.requireNonNull(pollInterval);
        if (pollInterval.isZero() || pollInterval.isNegative()) {
            throw new IllegalArgumentException(POLL_INTERVAL_MUST_BE_POSITIVE);
        }
    }
}
