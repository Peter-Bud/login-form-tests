package com.example.logintests.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class BrowserSession implements AutoCloseable {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private final List<String> consoleErrors = new ArrayList<>();
    private final List<String> pageErrors = new ArrayList<>();
    private final List<String> failedRequests = new ArrayList<>();
    private boolean tracing;
    private boolean closed;

    public BrowserSession(TestConfig config) {
        playwright = Playwright.create();
        try {
            BrowserType browserType = switch (config.browserEngine()) {
                case CHROMIUM -> playwright.chromium();
                case FIREFOX -> playwright.firefox();
                case WEBKIT -> playwright.webkit();
            };
            browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(config.headless()));
            browserContext = browser.newContext();
            page = browserContext.newPage();
            page.setDefaultTimeout(config.timeout().toMillis());
            page.setDefaultNavigationTimeout(config.timeout().toMillis());
            page.onConsoleMessage(message -> {
                if ("error".equalsIgnoreCase(message.type())) {
                    consoleErrors.add(message.text());
                }
            });
            page.onPageError(pageErrors::add);
            page.onRequestFailed(request -> failedRequests.add(
                request.method() + " " + request.url() + " — " + request.failure()));
            browserContext.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
            tracing = true;
        } catch (RuntimeException exception) {
            closeQuietly(browserContext, exception);
            closeQuietly(browser, exception);
            closeQuietly(playwright, exception);
            throw exception;
        }
    }

    private static void closeQuietly(AutoCloseable resource, RuntimeException original) {
        if (resource == null) {
            return;
        }
        try {
            resource.close();
        } catch (Exception cleanupFailure) {
            original.addSuppressed(cleanupFailure);
        }
    }

    public Page page() {
        return page;
    }

    public List<String> consoleErrors() {
        return List.copyOf(consoleErrors);
    }

    public List<String> pageErrors() {
        return List.copyOf(pageErrors);
    }

    public List<String> failedRequests() {
        return List.copyOf(failedRequests);
    }

    public void stopTrace(Path output) {
        if (!tracing) {
            return;
        }
        tracing = false;
        if (output == null) {
            if (browserContext != null) {
                browserContext.tracing().stop();
            }
        } else {
            browserContext
                .tracing()
                .stop(new Tracing.StopOptions().setPath(output));
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        RuntimeException failure = null;
        try {
            stopTrace(null);
        } catch (RuntimeException exception) {
            failure = exception;
        }
        failure = closeResource(browserContext, failure);
        failure = closeResource(browser, failure);
        failure = closeResource(playwright, failure);
        if (failure != null) {
            throw failure;
        }
    }

    private static RuntimeException closeResource(AutoCloseable resource, RuntimeException failure) {
        if (resource == null) {
            return failure;
        }
        try {
            resource.close();
        } catch (Exception exception) {
            if (failure == null) {
                return exception instanceof RuntimeException runtimeException
                    ? runtimeException
                    : new RuntimeException(exception);
            }
            failure.addSuppressed(exception);
        }
        return failure;
    }
}
