package com.isep.battlearena.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Battle page (/battle).
 * STEP 1: to complete. Real identifiers of the page:
 *   - attack buttons: attribute data-attack-type (QUICK_STRIKE, POWER_STRIKE, FIREBALL)
 *   - potion checkbox: #use-potion-checkbox
 *   - HP of fighter 1 / 2: #fighter1-hp-value / #fighter2-hp-value
 *   - mana of fighter 1: #fighter1-mana-value
 *   - turn indicator: #active-fighter-indicator (text such as "Bram's turn")
 *   - end-of-match banner: #match-banner ; battle log: #battle-log
 *   - "Rematch" button, visible once the match is over: #reset-button
 * No assertion in this class.
 */
public class BattlePage {

    private final Page page;

    public BattlePage(Page page) {
        this.page = page;
        // TODO 1.5: wait for the URL to end with /battle (page.waitForURL("**/battle"))
    }

    /** @param attackType QUICK_STRIKE, POWER_STRIKE or FIREBALL */
    public BattlePage attack(String attackType) {
        // TODO 1.6: click the button whose data-attack-type equals attackType, return this
        throw new UnsupportedOperationException("TODO 1.6");
    }

    public BattlePage powerStrike() {
        return attack("POWER_STRIKE");
    }

    public BattlePage usePotion() {
        // TODO 1.7: check the potion checkbox, return this
        throw new UnsupportedOperationException("TODO 1.7");
    }

    public Locator fighter1Hp() {
        // TODO 1.8: return the locator of the HP of fighter 1
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator fighter2Hp() {
        // TODO 1.8: return the locator of the HP of fighter 2
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator fighter1Mana() {
        // TODO 1.8: return the locator of the mana of fighter 1
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator potionBox() {
        // TODO 1.8: return the locator of the potion checkbox
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator indicator() {
        // TODO 1.8: return the locator of the turn indicator
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator banner() {
        // TODO 1.8: return the locator of the end-of-match banner
        throw new UnsupportedOperationException("TODO 1.8");
    }

    public Locator log() {
        // TODO 1.8: return the locator of the battle log
        throw new UnsupportedOperationException("TODO 1.8");
    }

    /** The "Rematch" button is visible once the match is over: it goes back to the setup page. */
    public SetupPage rematch() {
        // TODO 1.9: click the Rematch button and return the setup page (new SetupPage(page))
        throw new UnsupportedOperationException("TODO 1.9");
    }
}
