package com.isep.battlearena.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.isep.battlearena.model.MatchState;

/**
 * STEP 2: unit tests of the match lifecycle. The state diagram is in the Javadoc of MatchStateMachine.
 *
 * A new machine is created before each test: no test depends on the state left by another one.
 * Tests cover the state rules from steps 2.7 and 2.8.
 */
class MatchStateMachineTest {

    private MatchStateMachine machine;

    @BeforeEach
    void newMachine() {
        machine = new MatchStateMachine();
    }

    private void startAndFinishWithVictoryOfAria() {
        machine.start();
        machine.recordTurnOutcome(true, false, "Aria", "Bram");
    }

    private void startAndFinishWithDraw() {
        machine.start();
        machine.recordTurnOutcome(false, false, "Aria", "Bram");
    }

    // ---------------------------------------------------------------- start

    @Test
    void aNewMatchIsNotStarted() {
        assertEquals(MatchState.NOT_STARTED, machine.getState());
        assertNull(machine.getWinnerName());
    }

    @Test
    void startMovesToInProgress() {
        machine.start();
        assertEquals(MatchState.IN_PROGRESS, machine.getState());
    }

    @Test
    void startingTwiceIsRejected() {
        machine.start();
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> machine.start());
        assertEquals("Match already started", error.getMessage());
    }

    @Test
    void aFinishedMatchCannotBeStartedWithoutReset() {
        startAndFinishWithVictoryOfAria();
        assertThrows(IllegalStateException.class, () -> machine.start());
    }

    // ---------------------------------------------------------------- turn outcome

    @Test
    void bothFightersAliveKeepsTheMatchInProgress() {
        machine.start();
        machine.recordTurnOutcome(true, true, "Aria", "Bram");
        assertEquals(MatchState.IN_PROGRESS, machine.getState());
        assertNull(machine.getWinnerName());
    }

    @Test
    void whenFighterTwoFallsFighterOneWins() {
        startAndFinishWithVictoryOfAria();
        assertEquals(MatchState.VICTORY, machine.getState());
        assertEquals("Aria", machine.getWinnerName());
    }

    @Test
    void whenFighterOneFallsFighterTwoWins() {
        machine.start();
        machine.recordTurnOutcome(false, true, "Aria", "Bram");
        assertEquals(MatchState.VICTORY, machine.getState());
        assertEquals("Bram", machine.getWinnerName());
    }

    /** The draw cannot be reached by playing (one fighter attacks per turn) but the state machine must handle it. */
    @Test
    void bothFightersDownIsADrawWithoutWinner() {
        startAndFinishWithDraw();
        assertEquals(MatchState.DRAW, machine.getState());
        assertNull(machine.getWinnerName());
    }

    // ---------------------------------------------------------------- a finished match stays finished

    /** Javadoc: once the match has ended, a stray or duplicated call to recordTurnOutcome has no effect. */
    @Test
    void aVictoryIsNotUndoneByAnotherTurnOutcome() {
        startAndFinishWithVictoryOfAria();
        machine.recordTurnOutcome(true, true, "Aria", "Bram");
        assertEquals(MatchState.VICTORY, machine.getState());
        assertEquals("Aria", machine.getWinnerName());
    }

    @Test
    void aDrawIsNotUndoneByAnotherTurnOutcome() {
        startAndFinishWithDraw();
        machine.recordTurnOutcome(true, true, "Aria", "Bram");
        assertEquals(MatchState.DRAW, machine.getState());
        assertNull(machine.getWinnerName());
    }

    // ---------------------------------------------------------------- reset

    @Test
    void resetIsRejectedBeforeTheMatchStarts() {
        assertThrows(IllegalStateException.class, () -> machine.reset());
    }

    @Test
    void resetIsRejectedWhileTheMatchIsInProgress() {
        machine.start();
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> machine.reset());
        assertEquals("Match is not over yet", error.getMessage());
    }

    @Test
    void resetAfterAVictoryGoesBackToNotStartedWithoutWinner() {
        startAndFinishWithVictoryOfAria();
        machine.reset();
        assertEquals(MatchState.NOT_STARTED, machine.getState());
        assertNull(machine.getWinnerName());
    }

    @Test
    void resetAfterADrawGoesBackToNotStarted() {
        startAndFinishWithDraw();
        machine.reset();
        assertEquals(MatchState.NOT_STARTED, machine.getState());
    }
}
