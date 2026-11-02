package com.isep.battlearena.controller;

/**
 * JSON body posted by the browser when a player attacks. This is a
 * dumb data-transfer object: the JavaScript layer only fills it in
 * from the buttons the player clicked, it never decides whether the
 * attack is legal or how much damage it deals - that is entirely
 * CombatService's job.
 */
public class AttackRequest {

    private String attackType;
    private boolean usePowerPotion;

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public boolean isUsePowerPotion() {
        return usePowerPotion;
    }

    public void setUsePowerPotion(boolean usePowerPotion) {
        this.usePowerPotion = usePowerPotion;
    }
}
