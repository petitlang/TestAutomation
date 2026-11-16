package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

class DamageRulesTest extends UiTestBase {
    @ParameterizedTest(name = "{0} / {1} / potion={2}: {3} damage")
    @CsvSource({
            "RECRUIT, QUICK_STRIKE, true,  66", // (10 + 50) x 1.10 = 66.
            "ROGUE,   QUICK_STRIKE, false, 63", // (10 + 50) x 1.05 = 63.
            "MAGE,    FIREBALL,     true,  96", // (30 + 50) x 1.20 = 96.
            "WARRIOR, POWER_STRIKE, true,  84", // (20 + 50) x 1.20 = 84, after the cap.
            "MAGE,    QUICK_STRIKE, false, 66", // TC-04. (10 + 50) x 1.10 = 66.
            "WARRIOR, QUICK_STRIKE, false, 69"  // TC-05. (10 + 50) x 1.15 = 69.
    })
    void remainingHitPointsMatchTheSpecification(String characterClass, String attack,
                                                 boolean potion, int expectedDamage) {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1(characterClass, 50).startBattle();
        if (potion) {
            battle.usePotion();
        }
        battle.attack(attack);
        assertThat(battle.fighter2Hp()).hasText(String.valueOf(100 - expectedDamage));
    }
}
