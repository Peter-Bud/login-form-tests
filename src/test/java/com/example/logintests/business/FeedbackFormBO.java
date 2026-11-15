package com.example.logintests.business;

import com.example.logintests.core.PlaywrightContainer;
import com.example.logintests.pages.FeedbackFormPage;
import com.example.logintests.support.FeedbackFormConstants;

public final class FeedbackFormBO extends BaseBO {

    private static final String VALID_NAME = "Alex Tester";
    private static final String VALID_EMAIL = "alex.tester@example.com";
    private static final String VALID_MESSAGE = "The service was helpful.";

    private final FeedbackFormPage feedbackFormPage = new FeedbackFormPage();

    public FeedbackFormBO openFeedbackForm() {
        feedbackFormPage.navigateToFeedbackForm(PlaywrightContainer.config().requireFeedbackBaseUrl());
        return this;
    }

    public FeedbackFormBO sendCompleteFeedback() {
        feedbackFormPage
            .enterName(VALID_NAME)
            .enterEmail(VALID_EMAIL)
            .enterMessage(VALID_MESSAGE)
            .clickSend();
        return this;
    }

    public FeedbackFormBO sendFeedbackWithoutName() {
        feedbackFormPage
            .enterName("")
            .enterEmail(VALID_EMAIL)
            .enterMessage(VALID_MESSAGE)
            .clickSend();
        return this;
    }

    public FeedbackFormBO verifyFeedbackWasAccepted() {
        booleanValidator.verifyTrueCondition(feedbackFormPage.waitForConfirmationMessageDisplayed(),
            FeedbackFormConstants.CONFIRMATION_VISIBLE, softly)
            .verifyTrueCondition(feedbackFormPage.isConfirmationMessageTextPresent(), FeedbackFormConstants.CONFIRMATION_HAS_TEXT, softly);
        softly.assertAll();
        return this;
    }

    public FeedbackFormBO verifyMissingNameWasRejected() {
        booleanValidator.verifyTrueCondition(feedbackFormPage.isNameRequiredAndEmpty(), FeedbackFormConstants.NAME_REQUIRED_AND_EMPTY, softly)
            .verifyFalseCondition(feedbackFormPage.isConfirmationMessageDisplayed(), FeedbackFormConstants.CONFIRMATION_HIDDEN, softly);
        softly.assertAll();
        return this;
    }
}
