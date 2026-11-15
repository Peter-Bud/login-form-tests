package com.example.logintests.pages;

import static com.example.logintests.support.LazyWebElement.locateElement;

import com.example.logintests.support.FeedbackFormConstants;
import com.example.logintests.support.LazyWebElement;

public final class FeedbackFormPage extends BasePage {

    private final LazyWebElement nameField = locateElement(FeedbackFormConstants.NAME_FIELD_SELECTOR);
    private final LazyWebElement emailField = locateElement(FeedbackFormConstants.EMAIL_FIELD_SELECTOR);
    private final LazyWebElement messageField = locateElement(FeedbackFormConstants.MESSAGE_FIELD_SELECTOR);
    private final LazyWebElement sendButton = locateElement(FeedbackFormConstants.SEND_BUTTON_SELECTOR);
    private final LazyWebElement confirmationMessage = locateElement(FeedbackFormConstants.CONFIRMATION_SELECTOR);

    public void navigateToFeedbackForm(String baseUrl) {
        page.navigate(baseUrl);
    }

    public FeedbackFormPage enterName(String name) {
        enterTextIntoElement(nameField, name);
        return this;
    }

    public FeedbackFormPage enterEmail(String email) {
        enterTextIntoElement(emailField, email);
        return this;
    }

    public FeedbackFormPage enterMessage(String message) {
        enterTextIntoElement(messageField, message);
        return this;
    }

    public FeedbackFormPage clickSend() {
        clickOnVisibleElement(sendButton);
        return this;
    }

    public boolean isConfirmationMessageDisplayed() {
        return isElementDisplayed(confirmationMessage);
    }

    public boolean waitForConfirmationMessageDisplayed() {
        return waitForElementDisplayed(confirmationMessage);
    }

    public boolean isConfirmationMessageHidden() {
        return !isElementDisplayed(confirmationMessage);
    }

    public boolean isConfirmationMessageTextPresent() {
        return !getElementText(confirmationMessage).isBlank();
    }

    public boolean isNameRequiredAndEmpty() {
        return Boolean.TRUE.equals(nameField.get().evaluate("element => element.required && element.validity.valueMissing"));
    }
}
