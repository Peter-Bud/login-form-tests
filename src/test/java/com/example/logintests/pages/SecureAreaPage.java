package com.example.logintests.pages;

import static com.example.logintests.support.LazyWebElement.locateElement;

import java.time.Duration;

import com.example.logintests.support.LazyWebElement;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

public final class SecureAreaPage extends BasePage {

    private final LazyWebElement logoutLink = locateElement("a[href$='/practice-test-login/']");
    private final LazyWebElement heading = locateElement("text=\"Logged In Successfully\"");
    private final LazyWebElement successMessage = locateElement("text=\"Congratulations student. You successfully logged in!\"");

    public boolean isLogoutLinkDisplayed() {
        return isElementDisplayed(logoutLink);
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }

    public boolean isHeadingDisplayed() {
        return isElementDisplayed(heading);
    }

    public boolean waitForHeadingDisplayed() {
        return waitForElementDisplayed(heading);
    }

    public String getHeadingText() {
        return getElementText(heading);
    }

    public String getSuccessMessageText() {
        return getElementText(successMessage);
    }

    public void logout(Duration timeout) {
        page.waitForNavigation(
            new Page.WaitForNavigationOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(timeout.toMillis()),
            () -> clickOnVisibleElement(logoutLink));
    }
}
