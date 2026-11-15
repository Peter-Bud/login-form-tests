package com.example.logintests.support;

import org.assertj.core.api.SoftAssertions;

public final class TextValidator {

    public TextValidator() {}

    public TextValidator validateTextEquality(String actual, String expected, String message, SoftAssertions softly) {
        softly.assertThat(actual).as(message).isEqualTo(expected);
        return this;
    }
}
