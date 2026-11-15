package com.example.logintests.tests;

import com.example.logintests.business.LoginBO;
import com.example.logintests.core.BaseTest;
import com.example.logintests.core.TestConfig;
import com.example.logintests.data.LoginCredentials;
import com.example.logintests.data.LoginDataFactory;
import com.example.logintests.data.LoginError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class LoginTest extends BaseTest {

    @Test(description = "Valid login, logout returns to the login form, then login succeeds again")
    public void validUserCanSignInAndLogOut() {
        loginBO()
            .openLoginForm()
            .verifyLoginFormIsReady()
            .signIn(LoginDataFactory.validUser())
            .verifyAuthenticatedState()
            .logout()
            .verifyLoggedOutState()
            .verifyLoginFormIsReady()
            .signIn(LoginDataFactory.validUser())
            .verifyAuthenticatedState();
    }

    @Test(dataProvider = "invalidCredentials", description = "Invalid credentials show the field-specific error")
    public void invalidCredentialsAreRejected(LoginCredentials credentials, LoginError expectedError) {
        loginBO()
            .openLoginForm()
            .signIn(credentials)
            .verifyLoginError(expectedError);
    }

    @Test(description = "A user can recover from a failed login attempt")
    public void userCanRetryWithValidCredentialsAfterFailure() {
        loginBO()
            .openLoginForm()
            .signIn(LoginDataFactory.invalidPassword())
            .verifyLoginError(LoginError.PASSWORD)
            .signIn(LoginDataFactory.validUser())
            .verifyAuthenticatedState();
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        long seed = TestConfig.load().randomSeed();
        return new Object[][] {
            { LoginDataFactory.missingUsername(), LoginError.USERNAME },
            { LoginDataFactory.missingPassword(), LoginError.PASSWORD },
            { LoginDataFactory.missingCredentials(), LoginError.USERNAME },
            { LoginDataFactory.invalidUsername(), LoginError.USERNAME },
            { LoginDataFactory.randomInvalidUsername(seed), LoginError.USERNAME },
            { LoginDataFactory.invalidPassword(), LoginError.PASSWORD },
            { LoginDataFactory.randomInvalidPassword(seed), LoginError.PASSWORD },
        };
    }

    private LoginBO loginBO() {
        return new LoginBO();
    }
}
