package com.example.logintests.api;

public final class ApiConstants {

    public static final String AUTH_ENDPOINT = "/auth";
    public static final String BOOKING_ENDPOINT = "/booking";
    public static final String PATH_SEPARATOR = "/";
    public static final String AUTH_COOKIE = "token";
    public static final String INVALID_TOKEN = "invalid-token";
    public static final String BAD_CREDENTIALS = "Bad credentials";
    public static final String BASE_URL_PROPERTY = "api.baseUrl";
    public static final String BASE_URL_ENVIRONMENT = "API_BASE_URL";
    public static final String USERNAME_PROPERTY = "api.username";
    public static final String USERNAME_ENVIRONMENT = "API_USERNAME";
    public static final String PASSWORD_PROPERTY = "api.password";
    public static final String PASSWORD_ENVIRONMENT = "API_PASSWORD";
    public static final String DEFAULT_URL = "https://restful-booker.herokuapp.com";
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password123";
    public static final String RESPONSE_LOG = "{} {} returned status {}";
    public static final String RESPONSE_STATUS = "Response status";
    public static final String RESPONSE_MODEL = "Response model";
    public static final String RESPONSE_DATA = "Response data";
    public static final String BOOKING_ID = "Booking ID";
    public static final String AUTH_TOKEN = "Authentication token";

    private ApiConstants() {}
}
