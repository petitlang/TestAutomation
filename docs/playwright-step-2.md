# Playwright tutorial: step 2

Date: 2026-11-02. Java: 17.0.18.

## 1. Write the first test

Added [`FirstStepsTest.java`](https://github.com/petitlang/TestAutomation/blob/9d5b85a5625305cbd31c99965b08feb9be5d1ff9/battle-arena/src/test/java/com/isep/battlearena/tutorial/FirstStepsTest.java) under `src/test/java/com/isep/battlearena/tutorial`. This link shows the step 2 version; the file is removed in step 4.

The test opens http://localhost:8080 and checks that the `h1` heading says `Battle Arena`.

| Object | Job |
| --- | --- |
| Playwright | Talks to the browser driver. |
| Browser | Runs Chromium, with no visible window by default. |
| BrowserContext | Gives this test a fresh browser profile. |
| Page | Opens a tab where the test can act and check the page. |

The app must be started by hand before this test runs. We will run it in step 3.

## 2. Compile without the library

Before changing the POM, ran this from `battle-arena`:

```powershell
.\mvnw.cmd test-compile
```

The first output used the local language. We repeated the command with English output for the record. This setting was only used for the command:

```powershell
$env:MAVEN_OPTS = (($env:MAVEN_OPTS + ' -Duser.language=en -Duser.country=US -Dfile.encoding=UTF-8').Trim())
.\mvnw.cmd test-compile
```

The English run returned exit code 1, with 11 compile errors. Main messages, with file paths left out:

```text
package com.microsoft.playwright.assertions does not exist
package com.microsoft.playwright does not exist
cannot find symbol
11 errors
BUILD FAILURE
```

The missing symbols included `Playwright`, `Browser`, `BrowserContext` and `Page`. The failed goal was `maven-compiler-plugin:3.13.0:testCompile`. Maven reported 2.549 seconds for this run.

**P1. Why does the main code still compile?**

Only the new file in `src/test` uses Playwright. The main code in `src/main` does not use it, so the `compile` phase has no missing Playwright classes. The error appears later, when `testCompile` checks the test code.

## 3. Add the library and compile again

Added these properties to [`pom.xml`](../battle-arena/pom.xml), using the versions from the tutorial:

```xml
<gson.version>2.14.0</gson.version>
<playwright.version>1.63.0</playwright.version>
```

Then added:

```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>${playwright.version}</version>
    <scope>test</scope>
</dependency>
```

The version property keeps the Playwright version in one place. The Gson property replaces the older version managed by Spring Boot. The `test` scope makes Playwright available to test code and leaves it out of the app package.

Ran `test-compile` again. Result: **BUILD SUCCESS**, exit code 0. Maven compiled all three test files and reported 24.903 seconds. This proves that the imports and code compile. It does not prove that the browser test passes, since `test-compile` runs no tests.

## 4. Read the dependency tree

Ran:

```powershell
.\mvnw.cmd dependency:tree '-Dincludes=com.microsoft.playwright:*,com.google.code.gson:gson'
```

The Playwright part of the output was:

```text
com.isep:battle-arena:jar:1.0.0
\- com.microsoft.playwright:playwright:jar:1.63.0:test
   +- com.google.code.gson:gson:jar:2.14.0:test
   +- com.microsoft.playwright:driver:jar:1.63.0:test
   \- com.microsoft.playwright:driver-bundle:jar:1.63.0:test
```

**P2. What does Playwright bring with it?**

Two examples are Gson and the Playwright driver. They come from the dependencies listed by Playwright itself. A transitive dependency is a library needed by another library we added, so Maven brings it in for us. The tree also shows `driver-bundle`.

## 5. Check the old tests

Ran only the existing service tests:

```powershell
.\mvnw.cmd test '-Dtest=com.isep.battlearena.service.*Test'
```

Result: **46 tests, 9 failures, 0 errors, 0 skipped**. This matches step 1. Maven returns exit code 1 because of the nine known failures. No application source, existing unit test or workbook was changed.

## Time

Work time before the final save and push: **3 minutes 18 seconds** (11:02:52 to 11:06:10, Paris time). This includes writing the test, compiling before and after the POM change, reading the dependency tree, checking the old tests and writing these notes.

Step 2 is complete. Next: start the app and run only `FirstStepsTest`.
