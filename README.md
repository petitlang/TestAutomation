# TestAutomation

ISEP Test and Test Automation coursework archive by Mouzheng Li.

## Contents

- [`battle-arena/`](battle-arena/): the Maven / Java Battle Arena project, including the completed TD3a unit tests and JaCoCo configuration.
- [`TD3a_Deliverables_Mouzheng.xlsx`](TD3a_Deliverables_Mouzheng.xlsx): the TD3a deliverables workbook.

## Current checkpoint

Playwright tutorial step 6 completed on 2026-11-02. We changed the expected HP to 25, read the failure and trace, then restored 28. Both UI tests pass again. The failed and passing traces are saved separately. See the [step 1 notes](docs/playwright-step-1.md), [step 2 notes](docs/playwright-step-2.md), [step 3 notes](docs/playwright-step-3.md), [step 4 notes](docs/playwright-step-4.md), [step 5 notes](docs/playwright-step-5.md) and [step 6 notes](docs/playwright-step-6.md).

The UI test result is **2 tests, 0 failures, 0 errors, 0 skipped**. The existing unit-test baseline is still **46 tests, 9 failures, 0 errors, 0 skipped**. The unit-test failures find the bugs left in the app for the course. Application sources under `src/main` must remain unchanged.

## TD2 source and defect counts

TD3a uses our own TD2 workbook, `TD2_BattleArena_Student_Pack_Mouzheng_Yuefan.xlsx`, by Mouzheng LI and Yuefan LIU. All 11 case IDs and titles in the TD3a selection match our TD2 table. We keep these IDs for the next steps.

We compared our work with `TD2_BattleArena_KeyAnswers.xlsx`. Our main table has 25 cases; the teacher's example has 13. Different inputs can give different damage values, so we compare the rule and the inputs as well as the result.

Our TD2 log has 12 reports, but several reports have the same cause. The teacher groups the bugs into five causes. These are two different ways to count the results:

| Basis | Found by the current unit tests | Rate |
| --- | --- | --- |
| The five behavior groups in our TD2 summary | 4 of 5 | 80% |
| The five bug causes in the teacher's answer | 5 of 5 | 100% |

The 80% uses the bugs we had already recorded in TD2. It counts a state update before `start()` as a separate behavior, and our current unit tests do not check that call. It does not count the negative-power bug found later in TD3a.

Our TD2 invalid-power case used -1, 0 and 101. It did not show an attack healing the defender, and we marked it Blocked because the rule for rejecting invalid input was not clear. TD3a adds power -50: a Quick Strike deals negative damage and raises the defender's HP. This test finds the fifth bug cause in the teacher's answer.

Some severity and priority labels also differ. These small gaps in the TD2 data and report do not block the next labs. The damage rules stay the same, and the current tests find all five bug causes. We continue with our own workbook and case IDs. The aim is to learn how to choose tests, read failures and improve coverage.

## Run the project

Use JDK 17 and the included Maven Wrapper. From `battle-arena`, run on Windows:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

For the unit-test baseline only, use `.\mvnw.cmd test '-Dtest=com.isep.battlearena.service.*Test'`. For the two UI tests only, use `.\mvnw.cmd test '-Dtest=PlaywrightTutorialTest'`. The UI tests start their own app on a free port; `spring-boot:run` is only needed for manual play. A plain `test` selects both groups and reports the known unit-test failures. To compile without running tests, use `.\mvnw.cmd test-compile`. `FirstStepsTest` was removed after step 3; its code is still in the Git history.

On Linux / macOS, use `./mvnw` instead. The application runs at http://localhost:8080.

To generate the coverage report despite the expected test failures:

```powershell
.\mvnw.cmd test -Dmaven.test.failure.ignore=true
```

Open `battle-arena/target/site/jacoco/index.html` after the run. See the [project README](battle-arena/README.md) for course-specific details.

Build output, local IDE configuration and local environment files are excluded from version control.
