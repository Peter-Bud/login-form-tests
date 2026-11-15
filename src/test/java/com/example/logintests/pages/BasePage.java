package com.example.logintests.pages;

import com.example.logintests.core.PlaywrightContainer;
import com.example.logintests.support.LazyWebElement;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.Objects;

public abstract class BasePage {

    protected final Page page;

    protected BasePage() {
        this.page = Objects.requireNonNull(PlaywrightContainer.page());
    }

    public String getPageUrl() {
        return page.url();
    }

    protected void enterTextIntoElement(LazyWebElement element, String text) {
        element.get().fill(text);
    }

    protected void clickOnVisibleElement(LazyWebElement element) {
        Locator locator = element.get();
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.click(new Locator.ClickOptions().setNoWaitAfter(true));
    }

    protected boolean isElementDisplayed(LazyWebElement element) {
        return element.get().isVisible();
    }

    protected boolean waitForElementDisplayed(LazyWebElement element) {
        try {
            element.get().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            return true;
        } catch (TimeoutError exception) {
            return false;
        }
    }

    protected String getElementText(LazyWebElement element) {
        return element.get().innerText().trim();
    }
}
