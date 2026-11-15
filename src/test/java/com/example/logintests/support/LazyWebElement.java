package com.example.logintests.support;

import com.example.logintests.core.PlaywrightContainer;
import com.microsoft.playwright.Locator;
import java.util.Objects;
import java.util.function.Supplier;

public final class LazyWebElement {

    private final Supplier<Locator> locatorSupplier;

    private LazyWebElement(Supplier<Locator> locatorSupplier) {
        this.locatorSupplier = Objects.requireNonNull(locatorSupplier);
    }

    public static LazyWebElement locateElement(String selector) {
        return new LazyWebElement(() ->
            PlaywrightContainer.page().locator(selector)
        );
    }

    public Locator get() {
        return locatorSupplier.get();
    }
}
