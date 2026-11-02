package com.isep.battlearena.tutorial;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.ui.support.UiTestBase;

class PlaywrightTutorialTest extends UiTestBase {

    @Test
    void theSetupPageIsDisplayed() {
        page.navigate(baseUrl);
        assertThat(page.locator("h1")).hasText("Battle Arena");
        assertThat(page.locator("button.btn-start")).isVisible();
    }

    @Test
    void aWarriorUsingAPotionIsCappedAtTwentyPercent() {
        // Arrange: Aria is Warrior with power 40. Keep Bram's defaults.
        page.navigate(baseUrl);
        page.locator("select[name='f1Class']").selectOption("WARRIOR");
        page.locator("input[name='f1AttackPower']").fill("40");
        page.locator("button.btn-start").click();
        page.waitForURL("**/battle");

        // Act: use the potion, then click Power Strike.
        page.locator("#use-potion-checkbox").check();
        page.locator("#attack-power").click();

        // Assert: (20 + 40) x 1.20 = 72 damage, so Bram has 28 HP.
        assertThat(page.locator("#fighter2-hp-value")).hasText("28");
        assertThat(page.locator("#battle-log")).containsText("Aria hits Bram for 72 damage.");
        assertThat(page.locator("#fighter1-mana-value")).hasText("85");
        assertThat(page.locator("#use-potion-checkbox")).not().isChecked();
        assertThat(page.locator("#active-fighter-indicator")).hasText("Bram's turn");
    }

    @ParameterizedTest(name = "{0} / {1} / potion={2}: {3} damage")
    @CsvSource({
            "WARRIOR, POWER_STRIKE, true,  72", // (20 + 40) x 1.20 = 72; bonus is capped.
            "WARRIOR, POWER_STRIKE, false, 69", // (20 + 40) x 1.15 = 69.
            "MAGE,    FIREBALL,     true,  84", // (30 + 40) x 1.20 = 84.
            "RECRUIT, QUICK_STRIKE, false, 50"  // (10 + 40) x 1.00 = 50.
    })
    void damageMatchesTheSpecification(String characterClass, String attack,
                                       boolean potion, int expectedDamage) {
        // Arrange: change Aria's class and keep her power at 40.
        page.navigate(baseUrl);
        page.locator("select[name='f1Class']").selectOption(characterClass);
        page.locator("input[name='f1AttackPower']").fill("40");
        page.locator("button.btn-start").click();
        page.waitForURL("**/battle");

        // Act: use the potion only when the row asks for it.
        if (potion) {
            page.locator("#use-potion-checkbox").check();
        }
        page.locator("[data-attack-type='" + attack + "']").click();

        // Assert: expected damage comes from the rules, not the app.
        assertThat(page.locator("#fighter2-hp-value")).hasText(String.valueOf(100 - expectedDamage));
        assertThat(page.locator("#battle-log"))
                .containsText("Aria hits Bram for " + expectedDamage + " damage.");
    }
}
