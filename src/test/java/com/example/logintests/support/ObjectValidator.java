package com.example.logintests.support;

import com.example.logintests.api.ApiConstants;

import static org.assertj.core.api.Assertions.assertThat;

public final class ObjectValidator {

    public void verifyObjectsEquality(Object actual, Object expected) {
        assertThat(actual).as(ApiConstants.RESPONSE_DATA).usingRecursiveComparison().isEqualTo(expected);
    }

    public void verifyPositiveId(Integer id) {
        assertThat(id).as(ApiConstants.BOOKING_ID).isNotNull().isPositive();
    }

    public void verifyTokenPresent(String token) {
        assertThat(token).as(ApiConstants.AUTH_TOKEN).isNotBlank();
    }
}
