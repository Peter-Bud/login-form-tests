package com.example.logintests.tests;

import com.example.logintests.business.FeedbackFormBO;
import com.example.logintests.core.BaseTest;
import org.testng.annotations.Test;

public final class FeedbackFormTest extends BaseTest {

    @Test(description = "Complete feedback form is submitted and confirmation is displayed")
    public void completeFeedbackIsSubmitted() {
        feedbackFormBO()
            .openFeedbackForm()
            .sendCompleteFeedback()
            .verifyFeedbackWasAccepted();
    }

    @Test(description = "Feedback is rejected when the mandatory Name field is empty")
    public void feedbackWithoutNameIsRejected() {
        feedbackFormBO()
            .openFeedbackForm()
            .sendFeedbackWithoutName()
            .verifyMissingNameWasRejected();
    }

    private FeedbackFormBO feedbackFormBO() {
        return new FeedbackFormBO();
    }
}
