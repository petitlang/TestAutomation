package com.isep.battlearena.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Setup page (root URL).
 * STEP 1: to complete. The field names come from the real form:
 *   - class list of fighter 1: select[name='f1Class']
 *   - attack power of fighter 1: input[name='f1AttackPower']
 *   - start button: button.btn-start
 * Page Object Model rules: locators are defined ONCE (constructor), no assertion in the page,
 * an action that changes page returns the next page.
 */
public class SetupPage {

    private final Page page;
    // TODO 1.1: declare the three Locator fields (fighter1Class, fighter1Power, startButton)

    public SetupPage(Page page) {
        this.page = page;
        // TODO 1.1: initialise the three locators
    }

    public SetupPage open(String baseUrl) {
        // TODO 1.2: open baseUrl, then return this
        throw new UnsupportedOperationException("TODO 1.2");
    }

    public SetupPage chooseFighter1(String characterClass, int attackPower) {
        // TODO 1.3: choose the class (selectOption), type the attack power (fill), return this
        throw new UnsupportedOperationException("TODO 1.3");
    }

    public BattlePage startBattle() {
        // TODO 1.4: click the start button, then return the next page (BattlePage)
        throw new UnsupportedOperationException("TODO 1.4");
    }

    // STEP 3: two more members, for the form validation tests.

    /** Types the attack power of fighter 1 without touching the rest of the form. */
    public SetupPage enterFighter1AttackPower(int attackPower) {
        // TODO 3.1: fill the attack power field with the value, return this
        throw new UnsupportedOperationException("TODO 3.1");
    }

    /**
     * Matches the attack power field of fighter 1 ONLY when the browser considers its value invalid.
     * The form declares min=1, max=100 and required; the CSS pseudo-class :invalid reflects that state.
     */
    public Locator fighter1AttackPowerWhenInvalid() {
        // TODO 3.1: return the locator "input[name='f1AttackPower']:invalid"
        throw new UnsupportedOperationException("TODO 3.1");
    }
}
