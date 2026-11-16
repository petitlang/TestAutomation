# TD3b pack

This pack goes **into your own project** (the Battle Arena project you used for TD3a, the Playwright tutorial and the
JMeter tutorial). It does not contain the application, the unit tests, `UiTestBase`, `BattleArenaApi` or the tutorial files: you already have them.

## What is in the pack

```
src/test/java/com/isep/battlearena/
    ui/pages/SetupPage.java                 Page Object to complete                      (step 1, form members in step 3)
    ui/pages/BattlePage.java                Page Object to complete                      (step 1)
    ui/BattleFlowTest.java                  journeys with Page Objects                   (step 1)
    ui/DamageRulesTest.java                 data-driven matrix of classes                (step 2)
    ui/CriticalHitTest.java                 boundary values 74, 75, 76                   (step 2)
    ui/StatefulJourneysTest.java            potion, defeat, rematch                      (step 3)
    ui/SetupFormTest.java                   validation of the setup form                 (step 3)
    ui/selenium/SeleniumSmokeTest.java      the same kind of journey with Selenium       (step 6; needs the Selenium dependency, step 0)
    ui/ApiPreparedJourneyTest.java          setup through the API (optional, track C)    (step 8)
    api/BattleApiTest.java                  API tests with Playwright (optional, track A) (step 8)
jmeter/battle-arena-api.jmx                 JMeter plan to complete                      (step 5)
jmeter/battle-arena-data-driven.jmx         JMeter plan driven by a CSV file (optional, track B) (step 8)
jmeter/data/damage-cases.csv                the data of that plan                        (step 8)
review/GeneratedTest.java.txt               AI-generated test to review (optional)       (step 9)
```

## How to install it

1. Unzip the pack in a **temporary folder**, outside your project.
2. Copy the folders `src`, `jmeter` and `review` **into the root of your project** (the folder that contains `pom.xml`). Accept the merge of
   folders: no file of the pack has the same name as a file of yours, except possibly in `jmeter` (your tutorial plan is called
   `battle-arena-tutorial.jmx`, the pack has other names).
3. Do **not** touch `src/main`, your unit tests, `UiTestBase.java`, `BattleArenaApi.java`, `PlaywrightTutorialTest.java`.
4. Go on with step 0 of the TD3b handout: the project does not compile yet, and this is the point of step 0.

If your project is not usable (broken, lost), the zip of the **reference start project** contains the same thing, already assembled.
