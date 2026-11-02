package com.isep.battlearena.controller;

import com.isep.battlearena.model.AttackResult;
import com.isep.battlearena.model.Fighter;
import com.isep.battlearena.model.MatchState;

/**
 * The full JSON payload sent to the browser after each action: the
 * result of the attack that just happened (if any), a snapshot of both
 * fighters, and the match's current state. The JavaScript layer reads
 * this and only this to decide what to animate; it holds no game logic
 * of its own.
 */
public class BattleStateView {

    private final AttackResult lastResult;
    private final FighterView fighter1;
    private final FighterView fighter2;
    private final String activeFighterName;
    private final MatchState matchState;
    private final String winnerName;

    public BattleStateView(AttackResult lastResult, Fighter fighter1, Fighter fighter2,
                            String activeFighterName, MatchState matchState, String winnerName) {
        this.lastResult = lastResult;
        this.fighter1 = FighterView.of(fighter1);
        this.fighter2 = FighterView.of(fighter2);
        this.activeFighterName = activeFighterName;
        this.matchState = matchState;
        this.winnerName = winnerName;
    }

    public AttackResult getLastResult() {
        return lastResult;
    }

    public FighterView getFighter1() {
        return fighter1;
    }

    public FighterView getFighter2() {
        return fighter2;
    }

    public String getActiveFighterName() {
        return activeFighterName;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public String getWinnerName() {
        return winnerName;
    }

    /** A read-only view of a Fighter, safe to serialize as JSON. */
    public static class FighterView {
        private final String name;
        private final String characterClass;
        private final int hp;
        private final int mana;
        private final int attackPower;

        private FighterView(String name, String characterClass, int hp, int mana, int attackPower) {
            this.name = name;
            this.characterClass = characterClass;
            this.hp = hp;
            this.mana = mana;
            this.attackPower = attackPower;
        }

        static FighterView of(Fighter fighter) {
            if (fighter == null) {
                return null;
            }
            return new FighterView(fighter.getName(), fighter.getCharacterClass(),
                    fighter.getHp(), fighter.getMana(), fighter.getAttackPower());
        }

        public String getName() {
            return name;
        }

        public String getCharacterClass() {
            return characterClass;
        }

        public int getHp() {
            return hp;
        }

        public int getMana() {
            return mana;
        }

        public int getAttackPower() {
            return attackPower;
        }
    }
}
