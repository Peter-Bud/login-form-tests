package com.example.logintests.data;

import java.util.Random;

public final class LoginDataFactory {

    private LoginDataFactory() {}

    public static LoginCredentials validUser() {
        return new LoginCredentials(LoginDataConstants.VALID_USERNAME, LoginDataConstants.VALID_PASSWORD);
    }

    public static LoginCredentials missingUsername() {
        return new LoginCredentials("", LoginDataConstants.VALID_PASSWORD);
    }

    public static LoginCredentials missingPassword() {
        return new LoginCredentials(LoginDataConstants.VALID_USERNAME, "");
    }

    public static LoginCredentials missingCredentials() {
        return new LoginCredentials("", "");
    }

    public static LoginCredentials invalidUsername() {
        return new LoginCredentials(LoginDataConstants.INVALID_USERNAME, LoginDataConstants.VALID_PASSWORD);
    }

    public static LoginCredentials randomInvalidUsername(long seed) {
        return new LoginCredentials(LoginDataConstants.INVALID_USERNAME_PREFIX + randomSuffix(seed),
            LoginDataConstants.VALID_PASSWORD);
    }

    public static LoginCredentials invalidPassword() {
        return new LoginCredentials(LoginDataConstants.VALID_USERNAME, LoginDataConstants.INVALID_PASSWORD);
    }

    public static LoginCredentials randomInvalidPassword(long seed) {
        return new LoginCredentials(LoginDataConstants.VALID_USERNAME,
            LoginDataConstants.INVALID_PASSWORD_PREFIX + randomSuffix(seed));
    }

    private static String randomSuffix(long seed) {
        return Long.toUnsignedString(new Random(seed).nextLong(), 36);
    }
}
