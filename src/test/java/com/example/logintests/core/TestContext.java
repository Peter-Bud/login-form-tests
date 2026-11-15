package com.example.logintests.core;

import com.microsoft.playwright.Page;
import java.nio.file.Path;
import java.util.List;

public final class TestContext implements AutoCloseable {

    private final TestConfig config;
    private final BrowserSession session;

    public TestContext(TestConfig config, BrowserSession session) {
        this.config = config;
        this.session = session;
    }

    public TestConfig config() {
        return config;
    }

    public Page page() {
        return session.page();
    }

    public List<String> consoleErrors() {
        return session.consoleErrors();
    }

    public List<String> pageErrors() {
        return session.pageErrors();
    }

    public List<String> failedRequests() {
        return session.failedRequests();
    }

    public void stopTrace(Path output) {
        session.stopTrace(output);
    }

    @Override
    public void close() {
        session.close();
    }
}
