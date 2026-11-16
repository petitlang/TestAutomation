package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

/** STEP 1: journeys written with the Page Objects. */
class BattleFlowTest extends UiTestBase {

    @Test
    void aWarriorUsingAPotionIsCappedAtTwentyPercent() {
        // TODO 1.10: rewrite YOUR tutorial test of TC-P1 with the Page Objects (Warrior, attack power 40,
        //   potion, Power Strike). Keep its five assertions: Bram's HP (28), the log line, Aria's mana (85),
        //   the potion box unticked, the turn indicator ("Bram's turn"). It should read like a scenario:
        //   BattlePage battle = new SetupPage(page).open(baseUrl).chooseFighter1(...).startBattle();
        //   battle.usePotion().powerStrike();
        //   assertThat(battle.fighter2Hp()).hasText("28");
        //   ...
    }

    @Test
    void aVictoryShowsTheBannerWithTheWinnerName() {
        // TODO 1.11: Recruit (RECRUIT) with attack power 76, one Quick Strike (QUICK_STRIKE).
        //   Calculation: (10 + 76) = 86, critical hit x 2 = 172 damage: Bram falls in one blow.
        //   Check Bram's HP (the floor is 0), that the banner is visible and shows "Aria wins the battle!".
    }
}
