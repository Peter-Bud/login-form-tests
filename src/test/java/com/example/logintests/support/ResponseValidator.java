package com.example.logintests.support;

import com.example.logintests.api.ApiConstants;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public final class ResponseValidator {

    private final Response response;

    public ResponseValidator(Response response) {
        this.response = response;
    }

    public ResponseValidator validateStatusCode(int expectedStatus) {
        assertThat(response.statusCode()).as(ApiConstants.RESPONSE_STATUS).isEqualTo(expectedStatus);
        return this;
    }

    public <T> T validateModelAndGet(Class<T> modelClass) {
        T model = response.as(modelClass);
        assertThat(model).as(ApiConstants.RESPONSE_MODEL).isNotNull();
        return model;
    }
}
