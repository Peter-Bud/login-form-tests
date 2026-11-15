package com.example.logintests.core;

import com.microsoft.playwright.Page;
import java.util.Objects;

public final class PlaywrightContainer {

    private static final ThreadLocal<TestContext> TEST_CONTEXT =
        new ThreadLocal<>();

    private PlaywrightContainer() {}

    public static void setTestContext(TestContext context) {
        TEST_CONTEXT.set(Objects.requireNonNull(context));
    }

    public static TestContext getTestContext() {
        TestContext context = TEST_CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("No test context is bound to the current thread");
        }
        return context;
    }

    public static Page page() {
        return getTestContext().page();
    }

    public static TestConfig config() {
        return getTestContext().config();
    }

    public static void removeTestContext() {
        TEST_CONTEXT.remove();
    }
}
