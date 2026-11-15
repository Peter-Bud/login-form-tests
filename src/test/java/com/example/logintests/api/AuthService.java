package com.example.logintests.api;

import com.example.logintests.data.AuthRequest;
import io.restassured.response.Response;

public final class AuthService {

    private final RestClient client = new RestClient();

    public Response authenticate(String username, String password) {
        return client.post(ApiConstants.AUTH_ENDPOINT, new AuthRequest(username, password));
    }
}
