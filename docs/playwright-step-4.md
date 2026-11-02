# Playwright tutorial: step 4

Date: 2026-11-02. Java: 17.0.18. Playwright: 1.63.0.

## What changed

Added the two support classes from the tutorial under `src/test/java/com/isep/battlearena/ui/support`:

- [`UiTestBase.java`](../battle-arena/src/test/java/com/isep/battlearena/ui/support/UiTestBase.java)
- [`BattleArenaApi.java`](../battle-arena/src/test/java/com/isep/battlearena/ui/support/BattleArenaApi.java)

The code follows the tutorial, with short English comments. No new dependency was needed. Java provides `HttpClient`, and the app already uses Jackson to read JSON.

Removed `FirstStepsTest.java`. It was a learning example that needed someone to start the app on port 8080. Its code is kept in the Git history.

## UiTestBase: shared setup

This is an abstract base class, not a test case. Later UI test classes will use `extends UiTestBase` and write their own `@Test` methods.

`@SpringBootTest(webEnvironment = RANDOM_PORT)` starts the app for those tests on a free port. Spring gives that port to the `port` field. `baseUrl` uses it, so the tests do not depend on a fixed port or a manually started app.

| Hook | When it runs | What it does |
| --- | --- | --- |
| `@BeforeAll` | Once before the tests in a class | Creates Playwright and launches one Chromium browser. |
| `@BeforeEach` | Before each test | Resets the match, creates a fresh context and page, and starts tracing. |
| `@AfterEach` | After each test | Saves the trace and closes that context. |
| `@AfterAll` | Once after the tests in a class | Closes Playwright and its browser. |

The browser is reused within a test class, because starting one costs time. Each test still has a fresh context, so it does not share browser cookies or local storage with another test.

The context records screenshots, page snapshots and source information. After each test, the trace is saved as `target/traces/<class>-<test>.zip`, for both passing and failing tests. Characters that are not safe in file names are replaced with `_`.

The fields `page`, `baseUrl` and `api` are `protected`, so a child test class can use them. The class is `abstract` because it holds shared setup, not a test to run on its own.

Browser options can now be passed on the command line:

```text
-Dheadless=false
-Dslowmo=500
```

This step adds the code that reads those options. We will use it with a real UI test in step 5.

## BattleArenaApi: prepare a clean match

This is a small HTTP client for test setup. It calls the running app; it is not an application service or a test case.

| Method | Job |
| --- | --- |
| `state()` | Reads `/api/state` as JSON. |
| `attack(...)` | Sends one attack to `/api/attack`. |
| `start(...)` | Starts a match through the same form endpoint as the setup page. |
| `ensureCleanState()` | Finishes a match if needed, then resets it. |

`ensureCleanState()` reads the match state:

1. `NOT_STARTED`: return; nothing needs to be done.
2. `IN_PROGRESS`: play a Quick Strike, then read the state again.
3. `VICTORY` or `DRAW`: call `/api/reset`, then check again.

The loop stops with an error after 50 tries if the match cannot be put back in a clean state.

The `catch` blocks do not hide errors. They add a clear message and throw a new error, keeping the original cause. If a thread is interrupted, the code also restores its interrupt flag.

## P4: Why Quick Strike? Why not reset straight away?

Quick Strike costs no mana, so it remains possible even when mana is zero. Fireball costs 25 mana and can stop working before the match ends. The reset endpoint only accepts a finished match, so calling it during a match would fail. We first finish the match with Quick Strikes, then reset it.

## Checks

Ran from `battle-arena`:

```powershell
.\mvnw.cmd clean test-compile
```

The first clean attempt could not delete the app log, because the app from step 3 was still running and writing to it. Stopped that app and ran the command again. Result: **BUILD SUCCESS**, exit code 0, Maven time 4.101 seconds. Four test-source files compiled: two unit-test classes and two support classes.

Using `clean` also removed the old `FirstStepsTest.class`, so Maven cannot run the deleted test by mistake.

Then ran:

```powershell
.\mvnw.cmd test '-Dtest=com.isep.battlearena.service.*Test'
```

| Class | Tests | Failures | Errors | Skipped |
| --- | --- | --- | --- | --- |
| CombatServiceTest | 32 | 7 | 0 | 0 |
| MatchStateMachineTest | 14 | 2 | 0 | 0 |
| Total | 46 | 9 | 0 | 0 |

The nine failing cases match the earlier baseline. Maven returned exit code 1 and `BUILD FAILURE` for these known failures, with a total time of 4.963 seconds. No application source, existing unit test, POM or workbook was changed.

This step checks that the helpers compile and that the old tests keep the same result. We have not run a UI test that extends the new base class yet, so there is no trace from these helpers yet. The manually started app is now stopped.

## Time

Work time before the final save and push: **2 minutes 39 seconds** (11:42:54 to 11:45:33, Paris time). This includes adding the helpers, fixing the locked-log clean issue, checking the build and unit tests, and writing these notes.

Next: add `PlaywrightTutorialTest`, inherit from `UiTestBase`, and check the setup page using `page` and `baseUrl`.
