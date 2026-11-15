package com.example.logintests.business;

import com.example.logintests.api.ApiConstants;

import com.example.logintests.api.ApiConfig;
import com.example.logintests.api.AuthService;
import com.example.logintests.data.AuthDataFactory;
import com.example.logintests.data.AuthResponse;
import com.example.logintests.data.InvalidAuthCase;
import com.example.logintests.data.LoginCredentials;
import com.example.logintests.support.ObjectValidator;
import com.example.logintests.support.ResponseValidator;

import static java.net.HttpURLConnection.HTTP_OK;

public final class AuthApiFacade {

    private final AuthService authService = new AuthService();
    private final ObjectValidator objectValidator = new ObjectValidator();

    public String authenticate() {
        AuthResponse response = new ResponseValidator(authService.authenticate(ApiConfig.username(), ApiConfig.password()))
            .validateStatusCode(HTTP_OK).validateModelAndGet(AuthResponse.class);
        objectValidator.verifyTokenPresent(response.token());
        return response.token();
    }

    public void verifyInvalidAuthentication(InvalidAuthCase authCase) {
        LoginCredentials credentials = AuthDataFactory.invalidCredentials(authCase);
        AuthResponse response = new ResponseValidator(authService.authenticate(credentials.username(), credentials.password()))
            .validateStatusCode(HTTP_OK).validateModelAndGet(AuthResponse.class);
        objectValidator.verifyObjectsEquality(response, new AuthResponse(null, ApiConstants.BAD_CREDENTIALS));
    }
}
