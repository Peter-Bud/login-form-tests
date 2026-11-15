# Theory

## Explain the principles of Agile testing and continuous integration. How does an Automation QA specialist contribute to quality in Agile, and how does this differ from manual testing roles?

Agile testing happens throughout development, not only after a feature is finished. The team agrees on
expected behavior, checks the feature as it is built, and uses feedback to find problems early. With
continuous integration, code changes are built and checked regularly, so the team gets quick feedback
when a change breaks something. API tests can provide quick feedback on business flows and permissions
while the broader UI suite runs.

I would discuss the API contract and acceptance criteria with developers and manual QA before
implementation. This helps the team agree on the expected responses, error handling, and test data early.

An Automation QA specialist works with the team to decide what is worth automating, builds and
maintains those checks, and helps investigate failures. This complements manual testing. Automation
is useful for repeatable scenarios, while exploratory testing and usability checks often need a
person to observe the product and apply judgment.

## What is a test specification and how do you translate manual test cases into automated test scripts? Describe key challenges in this process.

A test specification describes what needs to be checked, under which conditions, and what result is
expected. It usually includes preconditions, test data, steps, and expected results.

Before automating a manual case, I make sure the expected behavior is clear and the steps can be
repeated. Then I choose the right test level, prepare the data, implement the actions and checks
according to the project’s conventions, and run the script to confirm it checks the same behavior as
the original case.

One of the hardest parts can be reproducing the preconditions from a manual run. The database state,
user state, permissions, and environment configuration may all matter. For a payment flow, the order
state, payment method, and test gateway setup can affect the result. If these conditions are not
documented, the automated test may work only by chance or depend on a previous run. Other common
challenges include vague steps such as “make sure the page looks right,” fragile locators, and
unreliable test-data setup and cleanup. For example, a withdrawal test may need a verified user,
a completed deposit, and the correct limits. I would prepare those conditions through API helpers
and check the relevant database state when the scenario requires it.

For an integration scenario, I would also identify which services are involved and what should change
in each of them. For example, after a payment request, I may need to check the transaction status and
the user’s balance.

## Define what a test automation script is versus a manual test case. What types of tests typically benefit most from automation? Provide an example of a scenario where negative test cases should be automated before positive ones.

A manual test case is an instruction for a person: it describes the preconditions, steps, and
expected result. An automated test script performs those steps in code and compares the actual result
with what is expected.

Automation is most useful for stable checks that need to be repeated often, such as smoke and
regression tests, core business flows, and checks that run with multiple sets of data.

For a payment flow, I would first automate one successful payment as a baseline. Then I would cover
invalid card details, a declined payment, and repeated submission, where it is important to make sure
the customer is not charged twice. If the team is fixing a critical double-charge defect, that
negative scenario should be automated first for that fix, ahead of broader positive coverage. For
repeated requests, I would check the API responses and the resulting transactions and balance. To test
duplicate submission, I would send the requests close together and then check how many transactions
were created and how the balance changed.

## What is a test automation framework? Mention some popular frameworks in Java (with Spring Boot), and describe how you would design a modular, maintainable framework.

A test automation framework is a shared set of rules, components, and utilities for building and
running automated tests. Common Java choices include JUnit 5 or TestNG with Selenium or Playwright
for browser testing, REST Assured for APIs, and Spring Boot Test for Spring applications.

I would separate test scenarios, page objects or API clients, test data, configuration, and shared
utilities. Repeated operations should live in clear, reusable components, while the checks should
stay close to the scenario they verify. Environment settings and secrets should be provided
externally rather than stored in code. Tests should run locally and in CI in a consistent way,
produce clear results, and collect useful diagnostics when they fail. The design should fit the
product instead of adding layers the project does not need. For API tests, service classes can handle
requests, while facades combine business actions and checks. Data providers and helpers can prepare
users and other preconditions, with separate database access where the scenario needs it. I would use
request and response models for API data and reusable validators for status codes and response content.
This keeps individual tests focused on the business scenario.

## Explain the difference between bug severity and priority in an automation context. How do automated tests help detect regression bugs early?

Severity describes how seriously a defect affects the product or its users. Priority describes how
soon the team should fix it. For example, a defect in a rarely used administrative feature could
have high severity but lower priority if it does not block work. A small defect on a key landing
page during a campaign could have high priority. An automated test does not assign these values, but
it gives the team evidence about what broke and where.

Regression tests run after changes to check that existing behavior still works. When the relevant
suite runs in CI, the team gets an early signal and can investigate while the change that caused the
problem is still easy to identify. For example, after a withdrawal service change, a test can
check the API response and verify that the locked balance changes correctly after cancellation. When
a regression test fails, the request, response, and relevant logs help me investigate which part of
the flow changed before reporting the defect.

## What are advantages and disadvantages of automated testing? When might you avoid automating certain tests?

Automation runs checks quickly and consistently, and regression suites can run in CI after code
changes. That gives the team faster feedback and reduces repetitive work for manual testers, leaving
them more time for exploratory testing.

Automated tests also take time to build and maintain. Flaky checks, fragile locators, and difficult
test-data setup can consume that time and reduce confidence in the results. A feature whose
requirements are still changing and will likely be rewritten many times is usually a poor candidate
for early automation. Backend tests also depend on test users, database state, and other services,
so their setup and maintenance take time. API tests can also cover business rules and several input
combinations quickly, without repeating the whole browser flow for every case.

Automation also cannot replace usability judgment or exploration of unfamiliar behavior. Those
checks are better automated once the expected behavior is agreed and repeatable.

# Practical questions

## Write an automated test suite for a simple login form with fields "Username" and "Password" and a "Sign In" button on a web application. Describe your approach, tools, and priorities.

I used Java, Playwright, TestNG, and Gradle. I started with successful login and logout, then
covered missing and invalid credentials and a retry after a failed login. A TestNG DataProvider
runs the negative cases with different inputs.

The project separates tests, business actions, page objects, test data, and shared utilities.
It also records logs and collects screenshots and traces when a UI test fails.

I also added API tests with REST Assured for a separate booking service. They cover authentication,
booking creation, retrieval, update, deletion, and an unauthorized request. The API part uses
service classes, facades, models, data providers, and validators. The booking lifecycle test creates
a booking, retrieves and updates it, then deletes it and checks that it is no longer available.

[LoginTest.java](src/test/java/com/example/logintests/tests/LoginTest.java) ·
[BookingApiTest.java](src/test/java/com/example/logintests/tests/BookingApiTest.java)

## Write one positive and one negative automated test case (pseudo-code or real code snippet in Java) for testing a feedback form with mandatory fields: "Name", "Email", "Message", and a "Send" button. The form shows a confirmation message after submission.

The positive case fills all mandatory fields and checks the confirmation message. The negative
case leaves Name empty and checks that the form rejects the submission. The tests call the business
layer, while the page object contains the form interactions.

[FeedbackFormTest.java](src/test/java/com/example/logintests/tests/FeedbackFormTest.java)

This is a simplified example with a fictional page and locators; it is not intended to run. If a real
feedback API were available, I would also check the submission response and verify that invalid data
is rejected on the backend.

## Given a TV application with channels restricted by age ratings (G, PG, R, NC-17, 18+), how would you design automated tests to verify parental control filtering? Estimate test cases needed for 40 channels with various ratings and describe possible test data strategies.

I would keep the 40 channels in a test data set, with each channel’s ID and age rating. A TestNG
DataProvider can pass the parental-control level and expected channel visibility to the same
regression test. Five settings across 40 channels produce 200 channel-and-rating combinations,
which can be checked in a loop or through parameterized data. The results can be split into smaller
batches if that makes failures easier to diagnose.

The expected result should follow the product’s rules: channels at or below the selected limit are
available, and channels above it are blocked. I would also cover parental controls being switched
off, an incorrect PIN, attempts to change the limit without the PIN, opening a restricted channel
directly, and whether the setting remains after an app restart. Unrated channels need a separate
case. That gives about 10 to 12 distinct scenarios, with the main rating checks driven by the data
provider.

I would use a fixed data set for the regression suite so every run checks the same ratings and
boundaries. Random data can add extra coverage, but it should not replace those predictable checks.
Before setting expected results, I would confirm how the product treats 18+ and unrated channels.
If an API provides the channel list, I would also compare its response with the expected channels
for each setting, alongside the UI checks. I would also change the parental-control setting through
the application, retrieve the saved setting through the API, and check that the returned channel list
follows the new limit.

## Describe how you would perform automated compatibility testing of a web app across multiple browsers, operating systems, and screen resolutions. What tools and approaches would you use?

I would define the browser, operating-system, and screen-size matrix in the test configuration, then
run the same scenarios with different settings. For example, the matrix could include Chrome,
Firefox, and Safari on supported desktop systems, plus the mobile browsers and screen sizes the
product needs to support. BrowserStack or Selenium Grid can provide the required browser and
operating-system combinations.

Three browsers, three operating systems, and three screen sizes make 27 configurations. The same
regression scenario can run in each configuration, and the runs can execute in parallel. On each
code change, I would run a smaller set of the highest-priority combinations, then run the full
matrix regularly and before a release. A random sample with a recorded seed can be useful for a
quick build check, but the full matrix should still run regularly so every combination gets covered.

I would check the main user flows, forms, navigation, responsive layout, and keyboard and touch
interactions. Screen sizes around responsive breakpoints deserve specific coverage because that is
where content and controls are most likely to overlap or disappear.

## Create a checklist for automated smoke test cases for a video player embedded on a website like YouTube. What core functionalities would you automate, and why?

I would cover the basic actions a viewer expects from the player:

- The player loads and becomes ready for interaction.
- A known test video starts playing.
- Playback can be paused and resumed.
- The timeline advances during playback.
- Seeking moves playback to the selected point.
- Mute and volume controls work.
- Full-screen mode can be entered and exited.
- Captions can be enabled when available.
- An unavailable video shows an error instead of leaving the player stuck.

For a YouTube embed, the IFrame Player API can report when the player is ready, whether playback is
running or paused, and whether an error occurred. I would use a stable test video and avoid checking
ads or recommendations, since they can change independently of the website.

If the application has a video API, I would check that it returns the expected metadata and
availability, and that restricted content follows the access rules.

I would also check the integration between the application and the player: select a video, verify
that the expected video loads, and check that an unavailable video or backend error produces a clear
message.

## Using exploratory testing principles for automation, describe how you would identify, script, and report a bug when testing a new web app, for example, the https://www.vmedia.ca site.

For VMedia, I would spend a short time getting familiar with the site and trying the main flows a
regular visitor would use: registration, signing in, profile actions, and choosing a plan. I would
try them in a few browsers and screen sizes, using both expected and unusual inputs. I would also
refresh partway through a flow, go back a page, and try leaving a required field empty.

If I found a problem, I would reproduce it and write down the shortest steps that trigger it. The
report should say what I was trying to do, what I expected, what happened instead, which browser and
operating system I used, and any relevant test data. A screenshot or short recording can help show
the issue. I would also inspect the related API request and response, and include useful details in
the report. Once the expected behavior is clear and the problem is reproducible, I would automate
those steps as a regression test at the appropriate level.

For example, if selecting a plan fails, I would check the plan and account data sent in the request,
the backend response, and any available logs. I would include the relevant details in the bug report
and automate the reproducible check at the appropriate level.
