package com.isep.battlearena.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.isep.battlearena.model.AttackResult;
import com.isep.battlearena.model.AttackType;
import com.isep.battlearena.model.Fighter;

/**
 * STEP 2: unit tests of the damage rules (base of the test pyramid).
 *
 * No Spring, no browser, no shared state: each test builds its own objects, so the whole class
 * runs in a fraction of a second. Every expected value comes from the SPECIFICATION (the Javadoc
 * of CombatService and the rules of the TD), NEVER from what the code returns.
 *
 * Tests cover the rules from steps 2.1 to 2.6.
 * Do not modify anything under src/main.
 */
class CombatServiceTest {

    private final CombatService combat = new CombatService();

    private static Fighter fighter(String characterClass, int attackPower) {
        return new Fighter("Test", characterClass, attackPower); // 100 HP, 100 mana
    }

    // ---------------------------------------------------------------- critical hit threshold

    @ParameterizedTest(name = "attack power {0}: critical = {1}")
    @CsvSource({
            "74, false", // 74 < 75: no critical hit.
            "75, true",  // 75 >= 75: critical hit.
            "76, true"   // 76 >= 75: critical hit.
    })
    void criticalHitStartsAtSeventyFive(int attackPower, boolean expected) {
        assertEquals(expected, combat.isCriticalHit(attackPower));
    }

    // ---------------------------------------------------------------- class bonus

    @ParameterizedTest(name = "{0}: bonus {1}")
    @CsvSource({
            "RECRUIT, 0.00", // Recruit: +0%.
            "ROGUE,   0.05", // Rogue: +5%.
            "MAGE,    0.10", // Mage: +10%.
            "WARRIOR, 0.15"  // Warrior: +15%.
    })
    void classBonusMatchesTheSpecification(String characterClass, double expectedBonus) {
        assertEquals(expectedBonus, combat.getClassDamageBonus(characterClass), 1e-9);
    }

    /** "Class names are matched case-insensitively" (Javadoc of CombatService). */
    @ParameterizedTest(name = "\"{0}\" is recognised as the Mage class")
    @CsvSource({
            "MAGE", // Upper case: Mage bonus is 10%.
            "Mage", // Mixed case: same class, same bonus.
            "mage"  // Lower case: same class, same bonus.
    })
    void classNamesAreCaseInsensitive(String spelling) {
        assertEquals(0.10, combat.getClassDamageBonus(spelling), 1e-9);
    }

    // ---------------------------------------------------------------- mana

    @ParameterizedTest(name = "{0} mana, {1}: affordable = {2}")
    @CsvSource({
            "0,  QUICK_STRIKE, true",    // free attack
            "14, POWER_STRIKE, false",
            "15, POWER_STRIKE, true",    // exactly the cost
            "24, FIREBALL,     false",
            "25, FIREBALL,     true"
    })
    void anAttackNeedsEnoughMana(int mana, AttackType attackType, boolean expected) {
        Fighter attacker = fighter("RECRUIT", 50);
        attacker.setMana(mana);
        assertEquals(expected, combat.canAfford(attacker, attackType));
    }

    // ---------------------------------------------------------------- damage

    /**
     * Damage = (base + attack power) x (1 + min(class bonus + potion, 0.20)), rounded, then doubled if critical.
     * Attack power is 50 in every row. Write the calculation next to each row.
     */
    @ParameterizedTest(name = "{0} / {1} / potion={2}: {3} damage")
    @CsvSource({
            "RECRUIT, QUICK_STRIKE, true,  66",   // (10 + 50) x 1.10 = 66
            "MAGE,    FIREBALL,     true,  96",   // (30 + 50) x (1 + 0.10 + 0.10) = 96
            "ROGUE,   QUICK_STRIKE, false, 63",   // (10 + 50) x 1.05 = 63
            "WARRIOR, FIREBALL,     false, 92",   // (30 + 50) x 1.15 = 92
            "WARRIOR, POWER_STRIKE, true,  84",   // (20 + 50) x 1.20 = 84; 25% is capped at 20%.
            "RECRUIT, QUICK_STRIKE, false, 60"    // (10 + 50) x 1.00 = 60
    })
    void damageMatchesTheSpecification(String characterClass, AttackType attackType,
                                       boolean potion, int expectedDamage) {
        assertEquals(expectedDamage, combat.computeDamage(fighter(characterClass, 50), attackType, potion));
    }

    /** Critical hits double the damage, with or without potion (here: RECRUIT, class bonus 0). */
    @ParameterizedTest(name = "power {0}, potion={1}: {2} damage")
    @CsvSource({
            "74, false, 84",  // No critical hit: 10 + 74 = 84.
            "75, false, 170", // Critical hit: (10 + 75) x 2 = 170.
            "76, false, 172", // Critical hit: (10 + 76) x 2 = 172.
            "80, true,  198"  // Potion and critical hit: round((10 + 80) x 1.10) x 2 = 198.
    })
    void criticalHitDoublesTheDamage(int attackPower, boolean potion, int expectedDamage) {
        assertEquals(expectedDamage,
                combat.computeDamage(fighter("RECRUIT", attackPower), AttackType.QUICK_STRIKE, potion));
    }

    /**
     * An attack must never heal the defender. Attack power is specified between 1 and 100,
     * but what does the code do with a value outside this range?
     */
    @ParameterizedTest(name = "attack power {0}: the defender never gains HP")
    @CsvSource({
            "0",     // 10 + 0 = 10 damage
            "-10",   // 10 - 10 = 0 damage: the limit
            "-50"    // 10 - 50 = ?
    })
    void anAttackNeverHealsTheDefender(int attackPower) {
        Fighter attacker = fighter("RECRUIT", attackPower);
        Fighter defender = fighter("RECRUIT", 50);

        combat.resolveAttack(attacker, defender, AttackType.QUICK_STRIKE, false);

        assertTrue(defender.getHp() <= 100,
                "An attack must not heal the defender: power=" + attackPower
                        + ", HP=" + defender.getHp());
    }

    // ---------------------------------------------------------------- resolveAttack

    @Test
    void anAttackWithoutEnoughManaIsRefusedAndChangesNothing() {
        Fighter attacker = fighter("MAGE", 50);
        attacker.setMana(24);
        Fighter defender = fighter("RECRUIT", 50);

        AttackResult result = combat.resolveAttack(attacker, defender, AttackType.FIREBALL, false);

        assertFalse(result.isAttackPerformed());
        assertEquals("NOT_ENOUGH_MANA", result.getFailureReason());
        assertEquals(24, attacker.getMana());
        assertEquals(100, defender.getHp());
    }

    @Test
    void manaIsSpentOnlyWhenTheAttackIsPerformed() {
        Fighter attacker = fighter("RECRUIT", 50);
        Fighter defender = fighter("RECRUIT", 50);

        AttackResult result = combat.resolveAttack(attacker, defender, AttackType.POWER_STRIKE, false);

        assertTrue(result.isAttackPerformed());
        assertEquals(85, attacker.getMana()); // 100 - 15
        assertEquals(85, result.getAttackerManaAfter());
    }

    @Test
    void theDefenderLosesExactlyTheComputedDamage() {
        Fighter attacker = fighter("RECRUIT", 50);
        Fighter defender = fighter("RECRUIT", 50);

        AttackResult result = combat.resolveAttack(attacker, defender, AttackType.QUICK_STRIKE, false);

        assertEquals(60, result.getDamage());  // (10 + 50) x 1.00
        assertEquals(40, defender.getHp());    // 100 - 60
        assertEquals(40, result.getDefenderHpAfter());
    }

    @Test
    void hitPointsNeverGoBelowZero() {
        Fighter attacker = fighter("RECRUIT", 100); // critical: (10 + 100) x 2 = 220
        Fighter defender = fighter("RECRUIT", 50);
        defender.setHp(10);

        combat.resolveAttack(attacker, defender, AttackType.QUICK_STRIKE, false);

        assertEquals(0, defender.getHp());
    }
}
