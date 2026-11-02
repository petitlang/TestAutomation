package com.isep.battlearena.service;

import org.springframework.stereotype.Service;

import com.isep.battlearena.model.AttackResult;
import com.isep.battlearena.model.AttackType;
import com.isep.battlearena.model.Fighter;

/**
 * Computes the outcome of a single attack in a Battle Arena match.
 *
 * This is the class TD2 asks students to test. Every rule below is also
 * written out in the lab specification handed to students; some of these
 * rules are implemented incorrectly on purpose, so that only the right
 * test technique, applied deliberately, will expose the bug. Nothing
 * here should be "fixed" while working through the lab: the goal is to
 * find and report the defects, not to patch them.
 */
@Service
public class CombatService {

    /**
     * Specification: "An attack whose attacker's attack power is 75 or
     * higher is always a critical hit (double damage), regardless of
     * class or potion."
     */
    public static final int CRITICAL_HIT_THRESHOLD = 75;

    /** Specification: a power potion adds a flat +10% damage bonus. */
    public static final double POWER_POTION_BONUS = 0.10;

    /** Specification: total damage bonus (class + potion) never exceeds +20%. */
    public static final double MAX_TOTAL_DAMAGE_BONUS = 0.20;

    /**
     * Specification: "Class names are matched case-insensitively: 'mage',
     * 'Mage' and 'MAGE' must all be recognised as the Mage class."
     *
     * Specification: the four classes and their damage bonus are:
     * RECRUIT +0%, ROGUE +5%, MAGE +10%, WARRIOR +15%.
     *
     * @param characterClass the fighter's class name, as stored on {@link Fighter}
     * @return the class's damage bonus, as a fraction (0.10 = +10%)
     */
    public double getClassDamageBonus(String characterClass) {
        if (characterClass.equals("WARRIOR")) {
            return 0.15;
        } else if (characterClass.equals("MAGE")) {
            return 0.10;
        } else if (characterClass.equals("RECRUIT")) {
            return 0.0;
        }
        return 0.0;
    }

    /**
     * Specification: "An attack power of exactly 75, or anything above
     * it, always lands as a critical hit."
     *
     * @param attackPower the attacker's attack power
     * @return true if the attack is a guaranteed critical hit
     */
    public boolean isCriticalHit(int attackPower) {
        return attackPower > CRITICAL_HIT_THRESHOLD;
    }

    /**
     * Specification: "A fighter can only perform an attack if they have
     * enough mana to pay its mana cost. Mana is only spent when the
     * attack is actually performed."
     */
    public boolean canAfford(Fighter fighter, AttackType attackType) {
        return fighter.getMana() >= attackType.getManaCost();
    }

    /**
     * Computes the damage a given attack would deal, before it is
     * applied to the defender. Specification:
     * <ol>
     *   <li>Base damage = attack type's base damage + attacker's attack power.</li>
     *   <li>A class damage bonus is added (see {@link #getClassDamageBonus(String)}).</li>
     *   <li>If a power potion is used, an extra +10% is added.</li>
     *   <li>The total bonus (class + potion) is capped at +20%.</li>
     *   <li>If the attack is a critical hit, the resulting damage is doubled.</li>
     *   <li>Attack power is always a positive value between 1 and 100 (inclusive).</li>
     * </ol>
     */
    public int computeDamage(Fighter attacker, AttackType attackType, boolean usePowerPotion) {
        int base = attackType.getBaseDamage() + attacker.getAttackPower();

        double bonus = getClassDamageBonus(attacker.getCharacterClass());
        if (usePowerPotion) {
            bonus += POWER_POTION_BONUS;
        }
        double cappedBonus = Math.min(bonus, MAX_TOTAL_DAMAGE_BONUS);

        int damage = (int) Math.round(base * (1 + cappedBonus));

        if (isCriticalHit(attacker.getAttackPower())) {
            damage = damage * 2;
        }

        return damage;
    }

    /**
     * Resolves one full attack: checks the attacker can afford it, spends
     * the mana, computes the damage, and applies it to the defender's HP
     * (never below 0). Returns a result describing what happened so the
     * caller (a REST controller) can report it to the browser.
     */
    public AttackResult resolveAttack(Fighter attacker, Fighter defender, AttackType attackType,
                                       boolean usePowerPotion) {
        if (!canAfford(attacker, attackType)) {
            return AttackResult.failure(attacker.getName(), "NOT_ENOUGH_MANA");
        }

        attacker.setMana(attacker.getMana() - attackType.getManaCost());

        int damage = computeDamage(attacker, attackType, usePowerPotion);
        boolean critical = isCriticalHit(attacker.getAttackPower());

        int newHp = Math.max(0, defender.getHp() - damage);
        defender.setHp(newHp);

        return AttackResult.success(attacker.getName(), defender.getName(), damage, critical,
                newHp, attacker.getMana());
    }
}
