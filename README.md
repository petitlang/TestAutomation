# TestAutomation

ISEP Test and Test Automation coursework archive by Mouzheng Li.

## Contents

- [`battle-arena/`](battle-arena/): the Maven / Java Battle Arena project, including the completed TD3a unit tests and JaCoCo configuration.
- [`TD3a_Deliverables_Mouzheng.xlsx`](TD3a_Deliverables_Mouzheng.xlsx): the TD3a deliverables workbook.
- [`TD3b_Deliverables_Student.xlsx`](TD3b_Deliverables_Student.xlsx): the TD3b results, failure analysis, questions and ROI.
- [`TD3b_JMeter_Report.zip`](TD3b_JMeter_Report.zip): the TD3b JMeter HTML report. Extract it and open `jmeter-report/index.html`.
- [`TD3b_Submission_Mouzheng.zip`](TD3b_Submission_Mouzheng.zip): the project, completed workbook and zipped JMeter report for submission.

## TD3b

Required steps 0 to 7 are complete. See the [step notes](docs/td3b-progress.md). The workbook contains the required report and answers. The optional tracks stay as provided in the teacher's pack.

The final suite reports 77 tests, 11 failures, 0 errors and 4 skipped optional tests. That is 73 executed runs, including the 6 tutorial runs. The failures are the 9 known unit cases and the 2 expected UI cases.

From `battle-arena`, run the whole suite with JDK 17 and Chrome installed.

```powershell
.\mvnw.cmd test '-Dmaven.test.failure.ignore=true'
```

The application contains course defects. Read the test totals even when this command ends with BUILD SUCCESS. Reports are in `target/surefire-reports`, traces in `target/traces`, and coverage in `target/site/jacoco/index.html`.

For JMeter, restart the application with `.\mvnw.cmd spring-boot:run`. In another terminal, run the following command with an empty or new output folder.

```powershell
jmeter -n -t jmeter/battle-arena-api.jmx -l target/jmeter/td3b/results.jtl -e -o target/jmeter/td3b/report
```

The final plan checks the specification and deliberately reports sample 05 as failed. The console has 9 samplers, while the HTML report has 11 rows because it also saves two redirect sub-results.

## Tutorial checkpoint

All seven steps of the Playwright tutorial are complete as of 2026-11-02. The UI class has six passing test runs: the setup page, TC-P1 and four data rows. The failed HP experiment from step 6 is restored, and its trace is saved separately. See the [step 1 notes](docs/playwright-step-1.md), [step 2 notes](docs/playwright-step-2.md), [step 3 notes](docs/playwright-step-3.md), [step 4 notes](docs/playwright-step-4.md), [step 5 notes](docs/playwright-step-5.md), [step 6 notes](docs/playwright-step-6.md) and [step 7 notes](docs/playwright-step-7.md).

The UI test result is **6 tests, 0 failures, 0 errors, 0 skipped**. The existing unit-test baseline is still **46 tests, 9 failures, 0 errors, 0 skipped**. The unit-test failures find the bugs left in the app for the course. Application sources under `src/main` must remain unchanged.

## JMeter tutorial

The nine JMeter steps were completed on 2026-11-16. The [test plan](battle-arena/jmeter/battle-arena-tutorial.jmx) runs TC-J1, checks the JSON responses, extracts Bram's HP, resets the match and then sends 200 read-only requests. See the [step-by-step notes](docs/jmeter-tutorial.md) and the [illustrated report](https://docs.google.com/document/d/1HzdIst1yTTkMrh4jc-bvMok-zqacpNQwLi7KX1cxrRM/edit).

The final run has 208 samplers and 0 errors in the console. The JTL and HTML report have 210 rows because they also save the two redirect sub-results of the start request. The damage experiment with expected 85 is complete. The saved plan uses the correct value, 84.

Start Battle Arena separately with JDK 17 before running JMeter. From `battle-arena`, use the path to your JMeter installation.

```powershell
$env:JAVA_HOME = 'D:\Java\jdk-17'
New-Item -ItemType Directory -Force target/jmeter | Out-Null
& 'D:/Downloads/td3-jmeter-tools/apache-jmeter-5.6.3/bin/jmeter.bat' -n -t jmeter/battle-arena-tutorial.jmx -l target/jmeter/run.jtl -e -o target/jmeter/run-report
```

Use a new JTL name and an empty or new report folder for each run. Results stay under `target/jmeter`. Open `target/jmeter/run-report/index.html` to read the dashboard.

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

For the unit-test baseline only, use `.\mvnw.cmd test '-Dtest=com.isep.battlearena.service.*Test'`. For the six UI test runs only, use `.\mvnw.cmd test '-Dtest=PlaywrightTutorialTest'`. The UI tests start their own app on a free port; `spring-boot:run` is only needed for manual play. A plain `test` selects both groups and reports the known unit-test failures. To compile without running tests, use `.\mvnw.cmd test-compile`. `FirstStepsTest` was removed after step 3; its code is still in the Git history.

On Linux / macOS, use `./mvnw` instead. The application runs at http://localhost:8080.

To generate the coverage report despite the expected test failures:

```powershell
.\mvnw.cmd test -Dmaven.test.failure.ignore=true
```

Open `battle-arena/target/site/jacoco/index.html` after the run. See the [project README](battle-arena/README.md) for course-specific details.

Build output, local IDE configuration and local environment files are excluded from version control.
