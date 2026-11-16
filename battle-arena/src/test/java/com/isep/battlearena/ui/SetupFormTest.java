package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

/**
 * STEP 3: validation of the setup form.
 * Specification: "Attack power is always a positive value between 1 and 100 (inclusive)."
 * The form declares min=1, max=100 and required, and the browser reflects the state of the field with the CSS
 * pseudo-class :invalid. We check that state. We do NOT check the wording of the validation message: it depends
 * on the language of the browser.
 */
class SetupFormTest extends UiTestBase {

    @ParameterizedTest(name = "attack power {0} is refused by the form")
    @CsvSource({
            "101"
            // TODO 3.5: add the rows 0 and -5. For each value, say which boundary of the specification it tests.
    })
    void anAttackPowerOutsideTheRangeIsInvalid(int attackPower) {
        // TODO 3.5: open the setup page, type the attack power (enterFighter1AttackPower),
        //   check that the "invalid" locator of the Page Object matches exactly one element (hasCount(1)).
    }

    @ParameterizedTest(name = "attack power {0} is accepted by the form")
    @CsvSource({
            "1"
            // TODO 3.6: add the row 100 (the upper boundary).
    })
    void anAttackPowerAtTheBoundaryIsValid(int attackPower) {
        // TODO 3.6: same steps, but the "invalid" locator must match no element (hasCount(0)).
    }
}
