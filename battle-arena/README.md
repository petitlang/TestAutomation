# Battle Arena

Sample application of the ISEP course *Test and Test Automation* (II. 3525), used in **TD2** (test techniques),
**TD3a** (unit tests, code coverage and the Maven POM) and **TD3b** (UI tests, API tests and summary). It is a small
turn-based combat game that exists only so you have a realistic, running piece of software to test.

## What this is for

You will not be modifying this application (`src/main`). In TD2 you read its source code, designed test cases and reported
defects. In TD3a you write **unit tests** for it, and they live in the **same project**, under `src/test`. The application
contains deliberate defects (DEF1 to DEF5): your tests must **detect** them, not fix them. Never edit anything under `src/main`.

**Scope of the rules under test: the `com.isep.battlearena.service` package** (`CombatService` and `MatchStateMachine`).
The web controllers, templates, CSS and JavaScript only exist to let you see and play the game (and, in TD3b, to give
UI tests something to drive).

## Requirements

- Java 17 (JDK). Check with `java -version`.
- Nothing else for Maven: the project uses the Maven Wrapper (`./mvnw`, or `mvnw.cmd` on Windows).
- Internet access on the first run: Maven downloads itself and the dependencies.

## Running the application (optional in TD3a)

```
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows
```

Open http://localhost:8080 once you see `Started BattleArenaApplication`. Stop with `Ctrl+C`.
The unit tests do not need the application: they call the service classes directly.

## Project layout

```
src/main/java/com/isep/battlearena/
    controller/                         web controllers and JSON API (out of scope)
    model/                              plain data classes
    service/                            CombatService, MatchStateMachine  <-- the rules under test
src/main/resources/                     Thymeleaf pages, CSS, JavaScript
src/test/java/com/isep/battlearena/
    service/CombatServiceTest.java      unit tests of the damage rules          (step 2)
    service/MatchStateMachineTest.java  unit tests of the match lifecycle       (step 2)
pom.xml                                 dependencies, plugins: JUnit 5, JaCoCo  (step 1)
```

Every `TODO` carries a number (1.1, 2.3, 2.8, ...) that matches the step of the TD handout. Your IDE lists them in its TODO view.
Tests without a TODO are provided.

## Commands

All commands run from the project folder. On Windows replace `./mvnw` with `mvnw.cmd`.

```
./mvnw test                                                   # all the tests
./mvnw test -Dtest=CombatServiceTest                          # one class
./mvnw test -Dtest=CombatServiceTest,MatchStateMachineTest    # both unit test classes (a few seconds)
```

The first run downloads Maven, the dependencies and the plugins: do it on a good network.

### Code coverage (JaCoCo)

JaCoCo is configured in `pom.xml`: its agent records which lines and branches the tests execute. The `prepare-agent` execution is provided; the `report` execution is TODO 1.1.
Failing tests stop Maven before the report is written, so add the flag below when you want the report while some tests are red
(a pipeline would not use this flag):

```
./mvnw test -Dtest=CombatServiceTest,MatchStateMachineTest -Dmaven.test.failure.ignore=true
```

Open `target/site/jacoco/index.html`, then the package `com.isep.battlearena.service`, then the class. Green lines were executed,
yellow lines are partly covered (some branches missing), red lines were never executed.

### Reading a failure

The assertion message is in the console and in `target/surefire-reports/`. The stack trace tells you which line of your test failed.
Then read the rule in `src/main/java/com/isep/battlearena/service/` (reading is allowed, editing is not).

## Where are my results?

| What | Command | Where |
|---|---|---|
| The tests | `./mvnw test` | `target/surefire-reports/` (JUnit reports) |
| Coverage | `./mvnw test -Dmaven.test.failure.ignore=true` | `target/site/jacoco/index.html` (and `target/jacoco.exec`, binary) |

A non-zero exit code from `./mvnw test` means at least one test failed: this is what a pipeline reads. Keep this project as it is:
TD3b starts from it.

## Two rules specific to Battle Arena

1. Each unit test creates its own objects (`new CombatService()`, new fighters): there is no shared state to reset. This is what
   the UI tests of TD3b will need to work around, because the application keeps **one match** for everybody.
2. `/api/reset` is only allowed **once the match is over** (victory or draw). In the middle of a match it returns an error.

## Versions

JaCoCo 0.8.15, Spring Boot 3.3.4 (the JUnit 5 version is managed by Spring Boot). These were the current versions when the TD was written:
if Maven cannot resolve one, check the current versions on the project sites.
