package com.example.logintests.support;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import org.assertj.core.api.SoftAssertions;

public final class BooleanValidator {

    public BooleanValidator() {}

    public BooleanValidator verifyTrueCondition(boolean condition, String message, SoftAssertions softly) {
        softly.assertThat(condition).as(message).isTrue();
        return this;
    }

    public BooleanValidator verifyFalseCondition(boolean condition, String message, SoftAssertions softly) {
        softly.assertThat(condition).as(message).isFalse();
        return this;
    }

    public BooleanValidator verifyTrueConditionWithWait(BooleanSupplier condition, String message, SoftAssertions softly) {
        softly
            .assertThat(CustomWaits.waitForCondition(condition))
            .as(message)
            .isTrue();
        return this;
    }

    public BooleanValidator verifyTrueConditionWithWait(BooleanSupplier condition, String message, SoftAssertions softly,
                                                        Duration timeout) {
        softly
            .assertThat(CustomWaits.waitForCondition(condition, timeout))
            .as(message)
            .isTrue();
        return this;
    }

}
