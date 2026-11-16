package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

/**
 * STEP 2: a matrix of data rows. Attack power is fixed at 50.
 * The expected damage comes from the SPECIFICATION (base + power, class bonus, potion +10%,
 * cap at +20%), never from what the application displays.
 */
class DamageRulesTest extends UiTestBase {

    @ParameterizedTest(name = "{0} / {1} / potion={2}: {3} damage")
    @CsvSource({
            "RECRUIT, QUICK_STRIKE, true,  66",   // (10 + 50) x 1.10 = 66
            "ROGUE,   QUICK_STRIKE, false, 63",   // (10 + 50) x 1.05 = 63
            "MAGE,    FIREBALL,     true,  96",   // (30 + 50) x (1 + 0.10 + 0.10) = 96
            "WARRIOR, POWER_STRIKE, true,  84"    // (20 + 50) x (1 + min(0.15 + 0.10, 0.20)) = 84
            // TODO 2.2: add at least two rows taken from YOUR TD2 test case table,
            // recomputing each expected value from the specification. Write the calculation in a comment.
    })
    void remainingHitPointsMatchTheSpecification(String characterClass, String attack,
                                                 boolean potion, int expectedDamage) {
        // TODO 2.1: open the page, choose (class, attack power 50), start;
        //   use the potion if needed; attack;
        //   check that Bram's HP equal 100 - expectedDamage.
    }
}
