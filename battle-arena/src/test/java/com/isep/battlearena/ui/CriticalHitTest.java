package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

class CriticalHitTest extends UiTestBase {
    @ParameterizedTest(name = "attack power {0}: {1} HP left for Bram")
    @CsvSource({
            "74, 16", // 10 + 74 = 84 damage. 100 - 84 = 16 HP.
            "75, 0",  // (10 + 75) x 2 = 170 damage. HP floor is 0.
            "76, 0"   // (10 + 76) x 2 = 172 damage. HP floor is 0.
    })
    void theCriticalThresholdIsSeventyFive(int attackPower, int expectedHp) {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("RECRUIT", attackPower).startBattle();
        battle.attack("QUICK_STRIKE");
        assertThat(battle.fighter2Hp()).hasText(String.valueOf(expectedHp));
    }
}
