package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

class StatefulJourneysTest extends UiTestBase {
    @Test
    void thePotionIsSingleUse() {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("WARRIOR", 20).startBattle();
        battle.usePotion().powerStrike();
        // First hit: (20 + 20) x 1.20 = 48. Bram has 52 HP.
        assertThat(battle.fighter2Hp()).hasText("52");
        assertThat(battle.potionBox()).not().isChecked();

        battle.attack("QUICK_STRIKE");
        assertThat(battle.indicator()).hasText("Aria's turn");
        battle.powerStrike();
        // Second hit: (20 + 20) x 1.15 = 46. 52 - 46 = 6 HP.
        // Two Power Strikes cost 2 x 15 mana, leaving 70.
        assertThat(battle.fighter2Hp()).hasText("6");
        assertThat(battle.log()).containsText("Aria hits Bram for 46 damage.");
        assertThat(battle.fighter1Mana()).hasText("70");
    }

    @Test
    void bramWinsWhenAriaFalls() {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("RECRUIT", 1).startBattle();
        // Aria deals 11 per hit. Bram wins after his second Quick Strike.
        battle.attack("QUICK_STRIKE").attack("QUICK_STRIKE")
                .attack("QUICK_STRIKE").attack("QUICK_STRIKE");
        assertThat(battle.banner()).hasText("Bram wins the battle!");
        assertThat(battle.fighter1Hp()).hasText("0");
    }

    @Test
    void aRematchBringsBackTheSetupPageAndANewMatchCanStart() {
        BattlePage battle = new SetupPage(page).open(baseUrl)
                .chooseFighter1("RECRUIT", 76).startBattle();
        // (10 + 76) x 2 = 172 damage ends the first match.
        battle.attack("QUICK_STRIKE");
        assertThat(battle.banner()).hasText("Aria wins the battle!");

        SetupPage setup = battle.rematch();
        assertThat(page).hasURL(baseUrl + "/");
        assertThat(setup.startButton()).isVisible();
        BattlePage nextBattle = setup.startBattle();
        assertThat(nextBattle.fighter2Hp()).hasText("100");
    }
}
