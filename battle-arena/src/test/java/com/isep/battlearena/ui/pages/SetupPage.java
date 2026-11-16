package com.isep.battlearena.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** Actions and fields on the setup page. */
public class SetupPage {
    private final Page page;
    private final Locator fighter1Class;
    private final Locator fighter1Power;
    private final Locator startButton;
    private final Locator invalidPower;

    public SetupPage(Page page) {
        this.page = page;
        fighter1Class = page.locator("select[name='f1Class']");
        fighter1Power = page.locator("input[name='f1AttackPower']");
        startButton = page.locator("button.btn-start");
        invalidPower = page.locator("input[name='f1AttackPower']:invalid");
    }

    public SetupPage open(String baseUrl) {
        page.navigate(baseUrl);
        return this;
    }

    public SetupPage chooseFighter1(String characterClass, int attackPower) {
        fighter1Class.selectOption(characterClass);
        fighter1Power.fill(String.valueOf(attackPower));
        return this;
    }

    public BattlePage startBattle() {
        startButton.click();
        return new BattlePage(page);
    }

    public SetupPage enterFighter1AttackPower(int attackPower) {
        fighter1Power.fill(String.valueOf(attackPower));
        return this;
    }

    public Locator fighter1AttackPowerWhenInvalid() {
        return invalidPower;
    }

    public Locator startButton() {
        return startButton;
    }
}
