package com.example.logintests.data;

import com.example.logintests.support.LoginUiConstants;

public enum LoginError {
    USERNAME(LoginUiConstants.USERNAME_ERROR),
    PASSWORD(LoginUiConstants.PASSWORD_ERROR);

    private final String message;

    LoginError(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
