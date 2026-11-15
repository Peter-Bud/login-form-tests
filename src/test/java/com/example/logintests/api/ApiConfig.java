package com.example.logintests.api;

public final class ApiConfig {

    private ApiConfig() {}

    public static String baseUrl() {
        return value(ApiConstants.BASE_URL_PROPERTY, ApiConstants.BASE_URL_ENVIRONMENT, ApiConstants.DEFAULT_URL);
    }

    public static String username() {
        return value(ApiConstants.USERNAME_PROPERTY, ApiConstants.USERNAME_ENVIRONMENT, ApiConstants.DEFAULT_USERNAME);
    }

    public static String password() {
        return value(ApiConstants.PASSWORD_PROPERTY, ApiConstants.PASSWORD_ENVIRONMENT, ApiConstants.DEFAULT_PASSWORD);
    }

    private static String value(String property, String environment, String fallback) {
        String configured = System.getProperty(property);
        if (configured == null || configured.isBlank()) {
            configured = System.getenv(environment);
        }
        return configured == null || configured.isBlank() ? fallback : configured.trim();
    }
}
