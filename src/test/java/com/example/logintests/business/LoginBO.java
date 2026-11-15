package com.example.logintests.business;

import java.time.Duration;

import com.example.logintests.data.LoginCredentials;
import com.example.logintests.data.LoginError;
import com.example.logintests.pages.LoginPage;
import com.example.logintests.pages.SecureAreaPage;
import com.example.logintests.support.LoginUiConstants;

public final class LoginBO extends BaseBO {

    private static final Duration LOGOUT_TIMEOUT = Duration.ofSeconds(30);

    private final LoginPage loginPage = new LoginPage();
    private final SecureAreaPage secureAreaPage = new SecureAreaPage();

    public LoginBO openLoginForm() {
        loginPage.navigateToLoginPage();
        return this;
    }

    public LoginBO verifyLoginFormIsReady() {
        booleanValidator.verifyTrueCondition(loginPage.waitForLoginFormReady(),
            LoginUiConstants.LOGIN_FORM_READY, softly)
            .verifyTrueCondition(loginPage.isUsernameFieldDisplayed(), LoginUiConstants.USERNAME_FIELD_VISIBLE, softly)
            .verifyTrueCondition(loginPage.isPasswordFieldDisplayed(), LoginUiConstants.PASSWORD_FIELD_VISIBLE, softly)
            .verifyTrueCondition(loginPage.isSubmitButtonDisplayed(), LoginUiConstants.SUBMIT_BUTTON_VISIBLE, softly)
            .verifyTrueCondition(LoginUiConstants.PASSWORD_INPUT_TYPE.equals(loginPage.getPasswordFieldType()), LoginUiConstants.PASSWORD_FIELD_MASKED, softly);
        softly.assertAll();
        return this;
    }

    public LoginBO signIn(LoginCredentials credentials) {
        loginPage
            .enterUsername(credentials.username())
            .enterPassword(credentials.password())
            .clickOnSubmitButton();
        return this;
    }

    public LoginBO verifyLoginError(LoginError expectedError) {
        booleanValidator.verifyTrueCondition(loginPage.waitForErrorMessageDisplayed(),
            LoginUiConstants.LOGIN_ERROR_VISIBLE, softly)
            .verifyTrueCondition(loginPage.getPageUrl().contains(LoginUiConstants.LOGIN_PATH),
                LoginUiConstants.LOGIN_REMAINS_AFTER_INVALID_CREDENTIALS, softly)
            .verifyTrueCondition(loginPage.isUsernameFieldDisplayed(), LoginUiConstants.USERNAME_FIELD_VISIBLE, softly)
            .verifyTrueCondition(loginPage.isPasswordFieldDisplayed(), LoginUiConstants.PASSWORD_FIELD_VISIBLE, softly)
            .verifyTrueCondition(loginPage.isSubmitButtonDisplayed(), LoginUiConstants.SUBMIT_BUTTON_VISIBLE, softly)
            .verifyFalseCondition(secureAreaPage.isHeadingDisplayed(), LoginUiConstants.AUTHENTICATED_HEADING_HIDDEN, softly)
            .verifyFalseCondition(secureAreaPage.isSuccessMessageDisplayed(), LoginUiConstants.AUTHENTICATED_MESSAGE_HIDDEN, softly)
            .verifyFalseCondition(secureAreaPage.isLogoutLinkDisplayed(), LoginUiConstants.LOGOUT_LINK_HIDDEN, softly);
        textValidator.validateTextEquality(loginPage.getErrorMessageText(), expectedError.message(), LoginUiConstants.LOGIN_ERROR_TEXT, softly);
        softly.assertAll();
        return this;
    }

    public LoginBO verifyAuthenticatedState() {
        booleanValidator.verifyTrueCondition(secureAreaPage.waitForHeadingDisplayed(),
            LoginUiConstants.SUCCESS_HEADING_VISIBLE, softly)
            .verifyTrueCondition(secureAreaPage.getPageUrl().contains(LoginUiConstants.LOGGED_IN_PATH),
                LoginUiConstants.SUCCESSFUL_LOGIN_URL, softly)
            .verifyTrueCondition(secureAreaPage.isSuccessMessageDisplayed(), LoginUiConstants.SUCCESS_MESSAGE_VISIBLE, softly)
            .verifyTrueCondition(secureAreaPage.isLogoutLinkDisplayed(), LoginUiConstants.LOGOUT_LINK_VISIBLE, softly);
        textValidator.validateTextEquality(secureAreaPage.getHeadingText(), LoginUiConstants.SUCCESS_HEADING, LoginUiConstants.SUCCESS_HEADING_TEXT, softly)
            .validateTextEquality(secureAreaPage.getSuccessMessageText(), LoginUiConstants.SUCCESS_MESSAGE, LoginUiConstants.SUCCESS_MESSAGE_TEXT, softly);
        softly.assertAll();
        return this;
    }

    public LoginBO logout() {
        secureAreaPage.logout(LOGOUT_TIMEOUT);
        return this;
    }

    public LoginBO verifyLoggedOutState() {
        booleanValidator.verifyTrueCondition(loginPage.getPageUrl().contains(LoginUiConstants.LOGIN_PATH),
            LoginUiConstants.LOGOUT_RETURNED_TO_LOGIN, softly)
            .verifyTrueCondition(loginPage.isUsernameFieldDisplayed(), LoginUiConstants.USERNAME_FIELD_VISIBLE, softly)
            .verifyTrueCondition(loginPage.isPasswordFieldDisplayed(), LoginUiConstants.PASSWORD_FIELD_VISIBLE, softly)
            .verifyFalseCondition(secureAreaPage.isHeadingDisplayed(), LoginUiConstants.AUTHENTICATED_HEADING_HIDDEN, softly)
            .verifyFalseCondition(secureAreaPage.isSuccessMessageDisplayed(), LoginUiConstants.AUTHENTICATED_MESSAGE_HIDDEN, softly)
            .verifyFalseCondition(secureAreaPage.isLogoutLinkDisplayed(), LoginUiConstants.LOGOUT_LINK_HIDDEN, softly);
        softly.assertAll();
        return this;
    }
}
