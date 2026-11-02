package com.isep.battlearena.model;

/**
 * The three attacks a fighter can perform. Each attack has a fixed mana
 * cost and a fixed base damage before class bonuses, critical hits and
 * potions are applied by {@link com.isep.battlearena.service.CombatService}.
 */
public enum AttackType {

    QUICK_STRIKE(0, 10),
    POWER_STRIKE(15, 20),
    FIREBALL(25, 30);

    private final int manaCost;
    private final int baseDamage;

    AttackType(int manaCost, int baseDamage) {
        this.manaCost = manaCost;
        this.baseDamage = baseDamage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getBaseDamage() {
        return baseDamage;
    }
}
