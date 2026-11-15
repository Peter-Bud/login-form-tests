package com.example.logintests.data;

import com.example.logintests.api.ApiConfig;

public final class AuthDataFactory {

    private static final String INVALID_USERNAME = "invalid-user";
    private static final String INVALID_PASSWORD = "invalid-password";
    private static final String EMPTY = "";

    private AuthDataFactory() {}

    public static LoginCredentials invalidCredentials(InvalidAuthCase authCase) {
        return switch (authCase) {
            case USERNAME -> new LoginCredentials(INVALID_USERNAME, ApiConfig.password());
            case PASSWORD -> new LoginCredentials(ApiConfig.username(), INVALID_PASSWORD);
            case EMPTY -> new LoginCredentials(EMPTY, EMPTY);
        };
    }
}
