package com.example.logintests.core;

import com.example.logintests.support.FailureArtifacts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    protected TestContext context;

    @BeforeMethod(alwaysRun = true)
    public void startTestContext() {
        TestConfig config = TestConfig.load();
        context = new TestContext(config, new BrowserSession(config));
        PlaywrightContainer.setTestContext(context);
        LOGGER.info("Started browser session: browser={}, baseUrl={}", config.browserEngine(), config.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void stopTestContext(ITestResult result) {
        TestContext current = context;
        context = null;
        if (current == null) {
            return;
        }
        try {
            FailureArtifacts.capture(current, result);
        } finally {
            try {
                current.close();
            } catch (RuntimeException cleanupFailure) {
                LOGGER.error("Failed to clean up browser session", cleanupFailure);
                if (result.getThrowable() != null) {
                    result.getThrowable().addSuppressed(cleanupFailure);
                } else {
                    throw cleanupFailure;
                }
            } finally {
                PlaywrightContainer.removeTestContext();
            }
        }
    }
}
