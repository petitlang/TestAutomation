package com.isep.battlearena.model;

/**
 * A fighter taking part in a Battle Arena match. This is a plain mutable
 * data holder; all game rules live in {@code CombatService} and
 * {@code MatchStateMachine}, never here.
 */
public class Fighter {

    /** Valid range documented by the specification: 1-100 inclusive. */
    public static final int MAX_HP = 100;
    public static final int MAX_MANA = 100;

    private String name;

    /**
     * Stored as free text on purpose (see {@link CharacterClass}): the
     * setup form posts this value exactly as typed/selected, and
     * CombatService matches it against the canonical class names.
     */
    private String characterClass;

    private int hp;
    private int mana;

    /** Base attack power for this fighter, expected in range 1-100. */
    private int attackPower;

    public Fighter() {
    }

    public Fighter(String name, String characterClass, int attackPower) {
        this.name = name;
        this.characterClass = characterClass;
        this.attackPower = attackPower;
        this.hp = MAX_HP;
        this.mana = MAX_MANA;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
