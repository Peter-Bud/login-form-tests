package com.example.logintests.support;

import com.example.logintests.core.TestContext;
import com.microsoft.playwright.Page;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;

public final class FailureArtifacts {

    private static final Logger LOGGER = LogManager.getLogger(FailureArtifacts.class);
    private static final Pattern SENSITIVE_ASSIGNMENT = Pattern.compile(
        "(?i)(password|passwd|pwd|token|authorization|username|user_name|email|login)(\\s*[=:]\\s*)([^\\s&,;]+)");
    private static final Pattern URL_CREDENTIALS = Pattern.compile("(?i)(https?://)[^/@\\s]+:[^/@\\s]+@");
    private static final AtomicLong INVOCATION_SEQUENCE = new AtomicLong();

    private FailureArtifacts() {}

    public static void capture(TestContext context, ITestResult result) {
        if (
            context == null ||
            result == null ||
            result.getStatus() != ITestResult.FAILURE) {
            return;
        }

        CredentialRedactor redactor = CredentialRedactor.from(result);
        String testName = safeTestName(result);
        Path directory = context
            .config()
            .artifactDirectory()
            .resolve(testName)
            .resolve(invocationIdentity(result));
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            LOGGER.error("Could not create failure artifact directory for {}", testName);
            return;
        }

        captureScreenshot(context, directory.resolve("screenshot.png"), redactor);
        captureTrace(context, directory.resolve("trace.zip"), redactor);
        writeStackTrace(result, directory.resolve("stacktrace.txt"), redactor);
        writeBrowserDiagnostics(context, directory.resolve("browser-diagnostics.log"), redactor);

        LOGGER.error("Test {} failed; diagnostics saved to {}", testName, directory);
    }

    private static void captureScreenshot(TestContext context, Path target, CredentialRedactor redactor) {
        try {
            Page page = context.page();
            if (page == null || page.isClosed()) {
                writeFailure(target.resolveSibling("screenshot-error.txt"), "Page was unavailable or already closed.");
                return;
            }
            page.screenshot(new Page.ScreenshotOptions().setPath(target).setFullPage(true));
        } catch (Exception e) {
            writeFailure(target.resolveSibling("screenshot-error.txt"), safe(e.toString(), redactor));
        }
    }

    private static void captureTrace(TestContext context, Path target, CredentialRedactor redactor) {
        try {
            context.stopTrace(target);
            if (!Files.exists(target)) {
                writeFailure(target.resolveSibling("trace-error.txt"), "Tracing stopped without producing an archive.");
            }
        } catch (Exception e) {
            writeFailure(target.resolveSibling("trace-error.txt"), safe(e.toString(), redactor));
        }
    }

    private static void writeStackTrace(ITestResult result, Path target, CredentialRedactor redactor) {
        Throwable throwable = result.getThrowable();
        String trace;
        if (throwable == null) {
            trace =
                "TestNG marked the invocation as failed but supplied no Throwable.\n";
        } else {
            StringWriter buffer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(buffer));
            trace = buffer.toString();
        }
        writeFailure(target, redactor.redact(trace));
    }

    private static void writeBrowserDiagnostics(TestContext context, Path target, CredentialRedactor redactor) {
        StringBuilder content = new StringBuilder("Browser diagnostics for failed test invocation\n")
            .append("Captured at: ")
            .append(Instant.now())
            .append('\n');
        appendLines(content, "Console errors", context.consoleErrors(), redactor);
        appendLines(content, "Uncaught page errors", context.pageErrors(), redactor);
        appendLines(content, "Failed requests", context.failedRequests(), redactor);
        writeFailure(target, content.toString());
    }

    private static void appendLines(
            StringBuilder output, String heading, List<String> lines, CredentialRedactor redactor) {
        output.append('\n').append("## ").append(heading).append('\n');
        if (lines == null || lines.isEmpty()) {
            output.append("(none)\n");
            return;
        }
        for (String line : lines) {
            output
                .append(redactor.redact(line == null ? "(null)" : line))
                .append('\n');
        }
    }

    private static void writeFailure(Path target, String text) {
        try {
            Files.createDirectories(target.getParent());
            Files.writeString(target, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Could not write diagnostic file {}", target);
        }
    }

    private static String safeTestName(ITestResult result) {
        String name = result.getMethod() == null
            ? "unknown-test"
            : result.getMethod().getMethodName();
        return sanitize(name == null || name.isBlank() ? "unknown-test" : name);
    }

    private static String invocationIdentity(ITestResult result) {
        long started = result.getStartMillis() > 0
            ? result.getStartMillis()
            : System.currentTimeMillis();
        return (
            "invocation-" +
            started +
            "-thread-" +
            Thread.currentThread().getId() +
            "-" +
            INVOCATION_SEQUENCE.incrementAndGet()
        );
    }

    private static String sanitize(String value) {
        String sanitized = value
            .replaceAll("[^A-Za-z0-9._-]+", "_")
            .replaceAll("^_+|_+$", "");
        return sanitized.isBlank() ? "test" : sanitized;
    }

    private static String safe(String value, CredentialRedactor redactor) {
        return redactor.redact(value == null ? "(no message)" : value);
    }

    private static final class CredentialRedactor {

        private final List<String> values;

        private CredentialRedactor(List<String> values) {
            this.values = values
                .stream()
                .filter(value -> value != null && !value.isEmpty())
                .distinct()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
        }

        static CredentialRedactor from(ITestResult result) {
            List<String> secrets = new ArrayList<>();
            for (Object parameter : result.getParameters()) {
                collectParameter(parameter, secrets, 0);
            }
            return new CredentialRedactor(secrets);
        }

        private static void collectParameter(Object value, List<String> secrets, int depth) {
            if (value == null || depth > 2) return;
            if (value instanceof CharSequence || value instanceof Number) {
                String candidate = value.toString();
                if (!candidate.isBlank()) secrets.add(candidate);
                return;
            }
            Class<?> type = value.getClass();
            for (Field field : type.getDeclaredFields()) {
                String fieldName = field.getName().toLowerCase(Locale.ROOT);
                if (
                    !(fieldName.contains("password") ||
                        fieldName.contains("username") ||
                        fieldName.equals("user") ||
                        fieldName.contains("email") ||
                        fieldName.contains("login") ||
                        fieldName.contains("token"))
                ) continue;
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(value);
                    if (fieldValue != null) secrets.add(fieldValue.toString());
                } catch (RuntimeException | IllegalAccessException ignored) {}
            }
        }

        String redact(String value) {
            String redacted = URL_CREDENTIALS.matcher(value).replaceAll("$1[REDACTED]@");
            redacted = SENSITIVE_ASSIGNMENT.matcher(redacted).replaceAll("$1$2[REDACTED]");
            for (String secret : values) {
                redacted = redacted.replace(secret, "[REDACTED]");
            }
            return redacted;
        }
    }
}
