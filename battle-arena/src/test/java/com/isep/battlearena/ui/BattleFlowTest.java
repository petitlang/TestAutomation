package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

class BattleFlowTest extends UiTestBase {
    @Test
    void aWarriorUsingAPotionIsCappedAtTwentyPercent() {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("WARRIOR", 40).startBattle();
        battle.usePotion().powerStrike();

        // (20 + 40) x 1.20 = 72 damage. Mana cost is 15.
        assertThat(battle.fighter2Hp()).hasText("28");
        assertThat(battle.log()).containsText("Aria hits Bram for 72 damage.");
        assertThat(battle.fighter1Mana()).hasText("85");
        assertThat(battle.potionBox()).not().isChecked();
        assertThat(battle.indicator()).hasText("Bram's turn");
    }

    @Test
    void aVictoryShowsTheBannerWithTheWinnerName() {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("RECRUIT", 76).startBattle();
        battle.attack("QUICK_STRIKE");

        // (10 + 76) x 2 = 172 damage. HP stops at zero.
        assertThat(battle.fighter2Hp()).hasText("0");
        assertThat(battle.banner()).isVisible();
        assertThat(battle.banner()).hasText("Aria wins the battle!");
    }
}
