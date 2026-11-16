package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.pages.SetupPage;
import com.isep.battlearena.ui.support.UiTestBase;

/**
 * STEP 3: journeys made of several steps. The state of the match evolves, and some rules only show over time.
 * Compute every expected value from the SPECIFICATION before you run, and write the calculation in a comment.
 */
class StatefulJourneysTest extends UiTestBase {

    @Test
    void thePotionIsSingleUse() {
        // TODO 3.2: Aria is a Warrior with attack power 20.
        //   1. tick the potion and play a Power Strike: check Bram's HP and that the potion box is unticked again;
        //   2. Bram plays a Quick Strike (click the attack button once: the active fighter is Bram);
        //      check the indicator says it is Aria's turn;
        //   3. Aria plays a Power Strike WITHOUT ticking the potion: check Bram's HP, the log line,
        //      and Aria's mana (two Power Strikes).
        //   If the potion lasted over two attacks, which value would be different?
    }

    @Test
    void bramWinsWhenAriaFalls() {
        // TODO 3.3: Aria is a Recruit with attack power 1, Bram keeps the default values.
        //   Click Quick Strike four times (Aria, Bram, Aria, Bram) and check the banner shows "Bram wins the battle!".
        //   Why four clicks, and why do you not need to know Bram's exact damage here?
    }

    @Test
    void aRematchBringsBackTheSetupPageAndANewMatchCanStart() {
        // TODO 3.4: Recruit with attack power 76, one Quick Strike: Aria wins (banner).
        //   Click Rematch (BattlePage.rematch()), check the URL is the root of the application and the start button
        //   is visible, then start a new battle and check that Bram has 100 HP.
        //   Hint for the URL: assertThat(page).hasURL(baseUrl + "/").
    }
}
