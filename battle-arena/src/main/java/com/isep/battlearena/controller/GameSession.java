package com.isep.battlearena.controller;

import org.springframework.stereotype.Component;

import com.isep.battlearena.model.Fighter;
import com.isep.battlearena.service.MatchStateMachine;

/**
 * Holds the single, application-wide match currently being played (this
 * demo has no login and no multi-table support: one browser tab is one
 * match) and whose turn it is. Deliberately kept out of the
 * {@code service} package: it has no game rules of its own, it only
 * remembers which two Fighter objects are playing and hands turn order
 * back and forth. Not in scope for TD2.
 */
@Component
public class GameSession {

    private final MatchStateMachine matchStateMachine;

    private Fighter fighter1;
    private Fighter fighter2;
    private int activeFighterNumber = 1;

    public GameSession(MatchStateMachine matchStateMachine) {
        this.matchStateMachine = matchStateMachine;
    }

    public void startNewMatch(Fighter fighter1, Fighter fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.activeFighterNumber = 1;
        matchStateMachine.start();
    }

    public void resetMatch() {
        matchStateMachine.reset();
        this.fighter1 = null;
        this.fighter2 = null;
        this.activeFighterNumber = 1;
    }

    public Fighter getFighter1() {
        return fighter1;
    }

    public Fighter getFighter2() {
        return fighter2;
    }

    public Fighter getActiveFighter() {
        return activeFighterNumber == 1 ? fighter1 : fighter2;
    }

    public Fighter getWaitingFighter() {
        return activeFighterNumber == 1 ? fighter2 : fighter1;
    }

    public void switchActiveFighter() {
        activeFighterNumber = activeFighterNumber == 1 ? 2 : 1;
    }
}
