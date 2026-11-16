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
