# TD3b

The lab continues in the Battle Arena project used for TD3a and both tutorials. The required work is steps 0 to 7. Results and answers are in `TD3b_Deliverables_Student.xlsx`.

## Step 0 - Install the pack

The tutorial check passed with 6 tests, 0 failures and 0 errors. Java is 17.0.18 and Maven is 3.9.16.

The pack added 15 files. The first `test-compile` failed in `SeleniumSmokeTest.java` because `org.openqa.selenium` was missing. After adding `selenium-java` with test scope, `test-compile` passed. Spring Boot manages its version. The existing Gson property overrides the version managed by the same parent.

The tutorial JMeter plan had local changes saved by JMeter. They add saved defaults and change the XML layout. The requests, data, expected damage 84 and reset remain the same.

Generated build output is under `battle-arena/target/td3b`. The final submission includes the JMeter HTML report as a separate ZIP.

## Step 1 - Page Objects

SetupPage holds the form fields and start action. BattlePage holds the attack actions, result locators and rematch action. Assertions stay in the tests.

BattleFlowTest passed both journeys. The temporary selector `button.btn-go` caused two 30-second timeouts, reported as 2 errors. Restoring `button.btn-start` in one line made both tests pass again. The tutorial has three raw uses of this selector.

## Step 2 - Damage data and boundaries

DamageRulesTest has six rows. The extra rows are our TD2 TC-04 and TC-05, selected here as UI integration checks for Mage and Warrior without a potion. They add coverage of the class bonuses without the potion or cap hiding them.

Five rows pass. The Rogue row expects 37 HP but gets 40. CriticalHitTest has three rows: 74 and 76 pass, while 75 expects 0 HP and gets 15. These are the two application defects expected by the handout.

Class times from the standalone runs are 18.350 s for 6 damage rows, 16.457 s for 3 critical rows and 0.350 s for 32 CombatService tests. The unit class has the same 7 known failures. The UI times include class setup and the failed assertion waits.

## Step 3 - Journeys and form validation

All 8 runs passed: three match journeys and five form values. The potion journey leaves Bram at 52 HP, then 6 HP, with Aria at 70 mana. The defeat and rematch journeys also pass. The form marks 101, 0 and -5 as invalid, while 1 and 100 are valid.

## Step 4 - Failure analysis

The full suite reported 77 tests, 11 failures, 0 errors and 4 skipped optional tests. Of the failures, 9 are the old unit cases and 2 are the expected UI cases. Maven took 47.961 s and wrote the JaCoCo report with `-Dmaven.test.failure.ignore=true`.

The saved trace responses confirm damage 85, HP 15 and criticalHit=false at power 75. The Rogue row returns damage 60 and HP 40. The screenshots and battle logs agree with those responses. The workbook links them to our TD2 defects and to the unit tests. Selenium's unfinished journey is completed and checked in step 6.

## Step 5 - JMeter API checks

The original plan was run in the GUI after restarting Battle Arena. All nine samplers were green, including sample 05 with damage 55 and Aria HP 45. The specified Rogue bonus gives round((10 + 45) x 1.05) = 58 damage and 42 HP.

Added eight JSON assertions to samples 05 to 08, restarted the app and ran the plan on the command line. Sample 05 fails. The other samples pass, including sample 09 whose provided assertion accepts HTTP 500. This records the current invalid-reset behavior; the workbook discusses using 409 instead.

The console counts 9 samplers and 1 error (11.11%). The JTL and dashboard also count two redirect sub-results, so they show 11 rows and 1 error (9.09%). The HTML report has been checked and is saved in `TD3b_JMeter_Report.zip`. The optional load group stays disabled.

## Step 6 - Selenium and waits

Completed the journey with an explicit URL wait and HP wait. Both tests passed with a visible browser and headless. A temporary expected HP of 9 caused a five-second TimeoutException with current text 8.

Removed only the HP wait and repeated the journey 10 times, with a fresh browser and match each time. Nine passed; one read the old HP 100 instead of 8. The JUnit XML and class result count all 10 repetitions; Maven's final failure summary groups the repeated method under one name. Restoring the wait gave 2 passing tests again.

The compared journey methods each have 7 statements. The workbook times are method times: 0.517 s for Playwright TC-P1 in the step 4 suite and 2.383 s for the headless Selenium journey. Their data and assertions differ, so these two timings are examples, not a benchmark.
