package com.isep.battlearena.ui.pages;

import java.util.Map;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** Actions and fields on the battle page. */
public class BattlePage {
    private final Page page;
    private final Map<String, Locator> attacks;
    private final Locator fighter1Hp;
    private final Locator fighter2Hp;
    private final Locator fighter1Mana;
    private final Locator potionBox;
    private final Locator indicator;
    private final Locator banner;
    private final Locator log;
    private final Locator resetButton;

    public BattlePage(Page page) {
        this.page = page;
        page.waitForURL("**/battle");
        attacks = Map.of(
                "QUICK_STRIKE", page.locator("[data-attack-type='QUICK_STRIKE']"),
                "POWER_STRIKE", page.locator("[data-attack-type='POWER_STRIKE']"),
                "FIREBALL", page.locator("[data-attack-type='FIREBALL']"));
        fighter1Hp = page.locator("#fighter1-hp-value");
        fighter2Hp = page.locator("#fighter2-hp-value");
        fighter1Mana = page.locator("#fighter1-mana-value");
        potionBox = page.locator("#use-potion-checkbox");
        indicator = page.locator("#active-fighter-indicator");
        banner = page.locator("#match-banner");
        log = page.locator("#battle-log");
        resetButton = page.locator("#reset-button");
    }

    public BattlePage attack(String attackType) {
        attacks.get(attackType).click();
        return this;
    }

    public BattlePage powerStrike() {
        return attack("POWER_STRIKE");
    }

    public BattlePage usePotion() {
        potionBox.check();
        return this;
    }

    public Locator fighter1Hp() {
        return fighter1Hp;
    }

    public Locator fighter2Hp() {
        return fighter2Hp;
    }

    public Locator fighter1Mana() {
        return fighter1Mana;
    }

    public Locator potionBox() {
        return potionBox;
    }

    public Locator indicator() {
        return indicator;
    }

    public Locator banner() {
        return banner;
    }

    public Locator log() {
        return log;
    }

    public SetupPage rematch() {
        resetButton.click();
        return new SetupPage(page);
    }
}
