package com.isep.battlearena.model;

/**
 * The outcome of one attack, as computed by CombatService. This is what
 * the JSON API returns to the browser; the JavaScript layer only reads
 * these fields to animate the result, it never recomputes them.
 */
public class AttackResult {

    private final boolean attackPerformed;
    private final String failureReason;
    private final String attackerName;
    private final String defenderName;
    private final int damage;
    private final boolean criticalHit;
    private final int defenderHpAfter;
    private final int attackerManaAfter;

    private AttackResult(boolean attackPerformed, String failureReason, String attackerName,
                          String defenderName, int damage, boolean criticalHit,
                          int defenderHpAfter, int attackerManaAfter) {
        this.attackPerformed = attackPerformed;
        this.failureReason = failureReason;
        this.attackerName = attackerName;
        this.defenderName = defenderName;
        this.damage = damage;
        this.criticalHit = criticalHit;
        this.defenderHpAfter = defenderHpAfter;
        this.attackerManaAfter = attackerManaAfter;
    }

    public static AttackResult success(String attackerName, String defenderName, int damage,
                                        boolean criticalHit, int defenderHpAfter, int attackerManaAfter) {
        return new AttackResult(true, null, attackerName, defenderName, damage, criticalHit,
                defenderHpAfter, attackerManaAfter);
    }

    public static AttackResult failure(String attackerName, String reason) {
        return new AttackResult(false, reason, attackerName, null, 0, false, 0, 0);
    }

    public boolean isAttackPerformed() {
        return attackPerformed;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getAttackerName() {
        return attackerName;
    }

    public String getDefenderName() {
        return defenderName;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isCriticalHit() {
        return criticalHit;
    }

    public int getDefenderHpAfter() {
        return defenderHpAfter;
    }

    public int getAttackerManaAfter() {
        return attackerManaAfter;
    }
}
