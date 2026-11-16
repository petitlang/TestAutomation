package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

class SetupFormTest extends UiTestBase {
    @ParameterizedTest(name = "attack power {0} is refused by the form")
    @CsvSource({
            "101", // Just above the upper limit.
            "0",   // Just below the lower limit.
            "-5"   // A negative value, outside the valid range.
    })
    void anAttackPowerOutsideTheRangeIsInvalid(int attackPower) {
        SetupPage setup = new SetupPage(page).open(baseUrl)
                .enterFighter1AttackPower(attackPower);
        assertThat(setup.fighter1AttackPowerWhenInvalid()).hasCount(1);
    }

    @ParameterizedTest(name = "attack power {0} is accepted by the form")
    @CsvSource({
            "1",  // The valid lower limit.
            "100" // The valid upper limit.
    })
    void anAttackPowerAtTheBoundaryIsValid(int attackPower) {
        SetupPage setup = new SetupPage(page).open(baseUrl)
                .enterFighter1AttackPower(attackPower);
        assertThat(setup.fighter1AttackPowerWhenInvalid()).hasCount(0);
    }
}
