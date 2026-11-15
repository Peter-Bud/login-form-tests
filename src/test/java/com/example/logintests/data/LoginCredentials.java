package com.example.logintests.data;

public record LoginCredentials(String username, String password) {
    @Override
    public String toString() {
        return (
            "LoginCredentials[username=" + username + ", password=<redacted>]"
        );
    }
}
