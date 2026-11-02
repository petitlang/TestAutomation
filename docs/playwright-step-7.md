# Playwright tutorial: step 7

Date: 2026-11-02. Java: 17.0.18. Playwright: 1.63.0.

## One journey, four data rows

Added `damageMatchesTheSpecification` to [`PlaywrightTutorialTest.java`](../battle-arena/src/test/java/com/isep/battlearena/tutorial/PlaywrightTutorialTest.java). The existing setup-page test and TC-P1 stay in the class.

```java
@ParameterizedTest(name = "{0} / {1} / potion={2}: {3} damage")
@CsvSource({
        "WARRIOR, POWER_STRIKE, true,  72",
        "WARRIOR, POWER_STRIKE, false, 69",
        "MAGE,    FIREBALL,     true,  84",
        "RECRUIT, QUICK_STRIKE, false, 50"
})
```

Each row calls the same method with four values: class, attack, potion and expected damage. JUnit turns `true` and `false` into booleans and the damage into an integer. The `{0}` to `{3}` fields put these values in the test's display name.

Each row is a separate test run. It gets the base class's clean match, fresh browser context, page and trace. The rows do not play four turns in the same match.

## Expected values

Aria's attack power stays at 40 in every row. Bram starts with 100 HP and keeps the default Rogue class and attack power 45.

| Class | Attack | Potion | Calculation from the rules | Expected damage | Expected Bram HP |
| --- | --- | --- | --- | --- | --- |
| Warrior | Power Strike | Yes | `(20 + 40) x 1.20`; the 25% bonus is capped at 20% | 72 | 28 |
| Warrior | Power Strike | No | `(20 + 40) x 1.15` | 69 | 31 |
| Mage | Fireball | Yes | `(30 + 40) x (1 + 0.10 + 0.10)` | 84 | 16 |
| Recruit | Quick Strike | No | `(10 + 40) x 1.00` | 50 | 50 |

These values were worked out before running the test. They were not copied from the app's results.

The first row repeats TC-P1. We keep the separate TC-P1 method because it also checks mana, the potion box and the next turn. The parameterized method checks HP and the log for each row.

## How the method uses a row

The form uses `characterClass` instead of a fixed class. It still fills power 40 and clicks Start Battle.

```java
if (potion) {
    page.locator("#use-potion-checkbox").check();
}
page.locator("[data-attack-type='" + attack + "']").click();
```

If the row says `false`, the potion box stays unticked. The `attack` value selects the right button. For a Fireball row, the selector becomes `[data-attack-type='FIREBALL']`.

```java
assertThat(page.locator("#fighter2-hp-value"))
        .hasText(String.valueOf(100 - expectedDamage));
```

The expected HP is 100 minus the row's expected damage. `String.valueOf` turns that number into text for `hasText`. The log check also uses the row's expected damage.

## Run and results

From `battle-arena`, ran:

```powershell
.\mvnw.cmd test -Dtest=PlaywrightTutorialTest
```

Used the same process-local Java socket-folder and English-output options as in [step 5](playwright-step-5.md#local-java-option).

Result: **6 tests, 0 failures, 0 errors, 0 skipped**, exit code 0 and `BUILD SUCCESS`. The app started on port 55908. Maven took 24.779 seconds and the JUnit class took 16.198 seconds.

The six runs are one setup-page test, one full TC-P1 test and four data rows. There are three Java test methods, but six test runs.

Checked the browser request and response in each trace:

| Data row | HTTP status | Damage returned | Bram HP returned |
| --- | --- | --- | --- |
| Warrior / Power Strike / potion | 200 | 72 | 28 |
| Warrior / Power Strike / no potion | 200 | 69 | 31 |
| Mage / Fireball / potion | 200 | 84 | 16 |
| Recruit / Quick Strike / no potion | 200 | 50 | 50 |

The recorded values match the expected values above. Each damage case has one browser attack request. The setup-page trace has no attack request.

Also ran the service unit tests again:

```powershell
.\mvnw.cmd test '-Dtest=com.isep.battlearena.service.*Test'
```

They still report **46 tests, 9 failures, 0 errors, 0 skipped**. Maven returned exit code 1 for the nine known failures and took 4.880 seconds. No application source, helper, existing unit test, POM or workbook was changed.

## Saved results

- [JUnit text report](playwright-step-7-results.txt)
- [All six traces](playwright-step-7-traces.zip)

The trace download is an outer ZIP containing six trace ZIP files. Extract it first, then open one inner ZIP in Trace Viewer. Each row has its own file name with its class, attack, potion and damage values.

## P7: UI test and API test

The Mage / Fireball / potion rule can be checked at both levels. A UI test checks that a user can choose the values, click the button and see the right result on the page. An API test can check the server's response without starting a browser, so it is usually faster and less tied to page layout.

## Tutorial complete

The project now has the Playwright dependency, two support classes and six passing UI test runs. The old unit-test result is unchanged. P1 and P2 are in step 2, P3 in step 3, P4 in step 4, P5 in step 5, P6 in step 6 and P7 here. Each step also records its time.

## Time

Work time before the final save and push: **2 minutes 52 seconds** (12:13:59 to 12:16:51, Paris time). This includes adding the four data rows, running the UI and unit tests, checking and saving the six traces, and writing these notes.

Keep the project and workbook for TD3b. The optional extra data row was not added; these are the four rows required by the tutorial.
