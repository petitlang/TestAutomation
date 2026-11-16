package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

/**
 * STEP 2 (continued): boundary values of the critical hit, Recruit, Quick Strike.
 * Specification: from 75 upwards the attack is a critical hit (damage doubled).
 * Remaining HP are never below 0.
 */
class CriticalHitTest extends UiTestBase {

    @ParameterizedTest(name = "attack power {0}: {1} HP left for Bram")
    @CsvSource({
            "74, 16"
            // TODO 2.4: compute BY HAND, from the specification, the remaining HP for 75 and for 76,
            // then add the two rows. Justify your calculation in a comment.
    })
    void theCriticalThresholdIsSeventyFive(int attackPower, int expectedHp) {
        // TODO 2.3: same principle as DamageRulesTest: a Recruit with this attack power,
        //   one Quick Strike, then check Bram's HP.
    }
}
