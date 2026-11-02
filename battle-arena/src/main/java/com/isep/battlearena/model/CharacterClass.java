package com.isep.battlearena.model;

/**
 * The four playable character classes offered in the setup screen.
 * This enum only lists the canonical values used by the UI dropdown;
 * a fighter's class is stored and matched as plain text (see
 * {@link Fighter#getCharacterClass()}), which is exactly the field
 * {@link com.isep.battlearena.service.CombatService#getClassDamageBonus(String)}
 * is exercising. Kept intentionally as a String field on Fighter, not as
 * this enum type, so the lab can exercise equivalence partitioning and
 * decision table testing on raw text input the way a form field would
 * actually deliver it.
 */
public enum CharacterClass {

    RECRUIT,
    ROGUE,
    MAGE,
    WARRIOR
}
