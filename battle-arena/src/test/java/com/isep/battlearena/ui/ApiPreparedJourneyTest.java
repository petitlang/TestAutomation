package com.isep.battlearena.ui;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.isep.battlearena.ui.pages.BattlePage;
import com.isep.battlearena.ui.support.UiTestBase;

/**
 * STEP 8, TRACK C (optional): set up the match through the API (fast), verify through the browser (what the user
 * sees). The field "api" comes from UiTestBase. To start, remove the @Disabled line below.
 */
@Disabled("Track C (optional): remove this line when you start")
class ApiPreparedJourneyTest extends UiTestBase {

    @Test
    void theBattlePageShowsAMatchPreparedThroughTheApi() {
        // TODO C.1: api.start("WARRIOR", 40);  api.attack("POWER_STRIKE", true);
        //   then page.navigate(baseUrl + "/battle"), new BattlePage(page), and check Bram's HP, Aria's mana and
        //   the indicator. Compute the three expected values from the specification.
    }

    @Test
    void theBattlePageShowsAFinishedMatchWhenItIsLoaded() {
        // TODO C.2: api.start("RECRUIT", 1), then four Quick Strikes through the API (api.attack("QUICK_STRIKE", false)).
        //   Open /battle: the page is loaded AFTER the end of the match. Check the banner ("Bram wins the battle!")
        //   and Aria's HP. Which two attacks of Bram are enough to take Aria's 100 HP?
    }
}
