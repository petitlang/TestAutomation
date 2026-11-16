# TD3b

The lab continues in the Battle Arena project used for TD3a and both tutorials. The required work is steps 0 to 7. Results and answers are in `TD3b_Deliverables_Student.xlsx`.

## Step 0 - Install the pack

The tutorial check passed with 6 tests, 0 failures and 0 errors. Java is 17.0.18 and Maven is 3.9.16.

The pack added 15 files. The first `test-compile` failed in `SeleniumSmokeTest.java` because `org.openqa.selenium` was missing. After adding `selenium-java` with test scope, `test-compile` passed. Spring Boot manages its version. The existing Gson property overrides the version managed by the same parent.

The tutorial JMeter plan had local changes saved by JMeter. They add saved defaults and change the XML layout. The requests, data, expected damage 84 and reset remain the same.

Generated build output is under `battle-arena/target/td3b`. The final submission includes the JMeter HTML report as a separate ZIP.
