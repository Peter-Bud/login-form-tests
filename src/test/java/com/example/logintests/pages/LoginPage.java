package com.example.logintests.pages;

import static com.example.logintests.support.LazyWebElement.locateElement;

import com.example.logintests.core.PlaywrightContainer;
import com.example.logintests.support.LazyWebElement;
import com.example.logintests.support.LoginUiConstants;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

public final class LoginPage extends BasePage {

    private final LazyWebElement usernameField = locateElement("#username");
    private final LazyWebElement passwordField = locateElement("#password");
    private final LazyWebElement submitButton = locateElement("#submit");
    private final LazyWebElement errorMessage = locateElement("#error");

    public void navigateToLoginPage() {
        page.navigate(PlaywrightContainer.config().baseUrl(),
            new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    public LoginPage enterUsername(String username) {
        enterTextIntoElement(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        enterTextIntoElement(passwordField, password);
        return this;
    }

    public LoginPage clickOnSubmitButton() {
        clickOnVisibleElement(submitButton);
        return this;
    }

    public boolean waitForErrorMessageDisplayed() {
        return waitForElementDisplayed(errorMessage);
    }

    public boolean waitForLoginFormReady() {
        return waitForElementDisplayed(usernameField)
            && waitForElementDisplayed(passwordField)
            && waitForElementDisplayed(submitButton);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public String getErrorMessageText() {
        return getElementText(errorMessage);
    }

    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(usernameField);
    }

    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }

    public boolean isSubmitButtonDisplayed() {
        return isElementDisplayed(submitButton);
    }

    public String getPasswordFieldType() {
        return passwordField
            .get()
            .getAttribute(LoginUiConstants.INPUT_TYPE_ATTRIBUTE);
    }
}
