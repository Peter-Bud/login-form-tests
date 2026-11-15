# Login form UI tests

Standalone Java 21 / Gradle project for practicing browser UI automation against the public Practice Test Automation login page. It is isolated from the existing work framework: it shares no source files, configuration, or runtime dependencies with that repository. The demo page labels its submit button **Submit** rather than **Sign In**.

## Layout

- `core/` — runtime configuration, per-test context, browser lifecycle, and TestNG base class.
- `pages/` — page objects and common page access.
- `waits/` — bounded condition waits.
- `data/`, `checks/`, `validation/`, `tests/` — test data, project-style validators, check groups, and TestNG scenarios.
- `diagnostics/` — failure-only screenshots, traces, stack traces, and browser diagnostics.
- `src/test/resources/log4j2.xml` — console and file logging configuration.

All framework and test Java sources are under `src/test/java`; this project has no production application code.

## Requirements and commands

Install a Java 21 JDK and use the included Gradle Wrapper. Playwright downloads browser binaries into this project's `build/playwright-browsers/` directory on the first test run, keeping them separate from browsers used by other projects. The project uses TestNG, Playwright Java, AssertJ, and Log4j2; it does not use Allure or Qase.

```sh
./gradlew clean testClasses   # compile test sources
./gradlew test                # run the TestNG suite
./gradlew test --tests 'com.example.logintests.tests.LoginTest'
```

## Configuration

Each setting can be supplied as a JVM system property or its environment-variable equivalent. A nonblank system property takes precedence.

| System property | Environment variable | Default |
| --- | --- | --- |
| `login.baseUrl` | `LOGIN_BASE_URL` | `https://practicetestautomation.com/practice-test-login/` |
| `feedback.baseUrl` | `FEEDBACK_BASE_URL` | none; required to run feedback-form tests |
| `login.browser` | `LOGIN_BROWSER` | `chromium` |
| `login.headless` | `LOGIN_HEADLESS` | `true` |
| `login.timeoutMillis` | `LOGIN_TIMEOUT_MILLIS` | `10000` |
| `login.artifactDirectory` | `LOGIN_ARTIFACT_DIRECTORY` | `build/artifacts` |
| `login.randomSeed` | `LOGIN_RANDOM_SEED` | `20261112` |

Example: `./gradlew test -Dlogin.browser=firefox -Dlogin.headless=false`.

## Logs and failure artifacts

Log4j2 writes the test run log to `build/logs/login-tests.log` and also prints to the console. On a failed invocation, diagnostics are saved under `build/artifacts/<test-name>/<invocation-id>/`: `screenshot.png`, `trace.zip`, `stacktrace.txt`, and `browser-diagnostics.log`. Browser diagnostics include console errors, uncaught page errors, and failed requests. Credentials are redacted from diagnostic text, and passwords are not written to step logs. Successful invocations do not retain trace archives.

## API tests

`BookingApiTest` exercises the public Restful Booker API using REST Assured and TestNG. It covers
authentication, invalid credentials, booking creation and retrieval, updating, deletion, and an
unauthorized update. Each booking created by a test is cleaned up after the test. API tests do not
start a browser. The service is shared and its availability can affect the result.

```sh
./gradlew apiTest
```

The default API URL is `https://restful-booker.herokuapp.com`. Override it with `-Dapi.baseUrl=...`
or `API_BASE_URL`; use `api.username` / `API_USERNAME` and `api.password` / `API_PASSWORD` to
override the public demo credentials. Request logs contain the method, path, and status, without
logging credentials or tokens.
