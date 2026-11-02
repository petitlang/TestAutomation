# TestAutomation

ISEP Test and Test Automation coursework archive by Mouzheng Li.

## Contents

- [`battle-arena/`](battle-arena/): the Maven / Java Battle Arena project, including the completed TD3a unit tests and JaCoCo configuration.
- [`TD3a_Deliverables_Mouzheng.xlsx`](TD3a_Deliverables_Mouzheng.xlsx): the TD3a deliverables workbook.

## Current checkpoint

Archived on 2026-11-02, before starting the Playwright tutorial.

The existing unit-test baseline is **46 tests, 9 failures, 0 errors, 0 skipped**. The failures detect deliberate application defects and are expected for this coursework. Application sources under `src/main` must remain unchanged. Playwright has not been added yet.

## Run the project

Use JDK 17 and the included Maven Wrapper. From `battle-arena`, run on Windows:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

On Linux / macOS, use `./mvnw` instead. The application runs at http://localhost:8080.

To generate the coverage report despite the expected test failures:

```powershell
.\mvnw.cmd test -Dmaven.test.failure.ignore=true
```

Open `battle-arena/target/site/jacoco/index.html` after the run. See the [project README](battle-arena/README.md) for course-specific details.

Build output, local IDE configuration and local environment files are excluded from version control.
