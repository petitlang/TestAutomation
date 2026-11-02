package com.isep.battlearena.service;

import org.springframework.stereotype.Service;

import com.isep.battlearena.model.MatchState;

/**
 * Tracks the lifecycle of one Battle Arena match.
 *
 * Specification (this is the state diagram students are given):
 * <pre>
 *   NOT_STARTED --start()--&gt; IN_PROGRESS
 *   IN_PROGRESS --recordTurnOutcome(), both still alive--&gt; IN_PROGRESS
 *   IN_PROGRESS --recordTurnOutcome(), exactly one alive--&gt; VICTORY
 *   IN_PROGRESS --recordTurnOutcome(), neither alive--&gt; DRAW
 *   VICTORY     --reset()--&gt; NOT_STARTED
 *   DRAW        --reset()--&gt; NOT_STARTED
 * </pre>
 * Every other transition is invalid and must be rejected: in particular,
 * once a match has reached VICTORY or DRAW it is over, and no further
 * call to {@code recordTurnOutcome} should ever move it back to
 * IN_PROGRESS. As with {@link CombatService}, this class is the target
 * of the lab: some of the logic below does not actually match the
 * specification above.
 */
@Service
public class MatchStateMachine {

    private MatchState state = MatchState.NOT_STARTED;
    private String winnerName;

    public MatchState getState() {
        return state;
    }

    public String getWinnerName() {
        return winnerName;
    }

    /**
     * Starts a new match. Specification: only legal from NOT_STARTED.
     */
    public void start() {
        if (state != MatchState.NOT_STARTED) {
            throw new IllegalStateException("Match already started");
        }
        state = MatchState.IN_PROGRESS;
        winnerName = null;
    }

    /**
     * Called once per turn, after damage has been applied to both
     * fighters, to update the match state from the fighters' current
     * HP. Specification: this call has no effect once the match has
     * already ended (VICTORY or DRAW) - a stray or duplicated call must
     * be ignored, not allowed to resurrect a finished match.
     *
     * @param fighter1Alive whether fighter 1's HP is still above 0
     * @param fighter2Alive whether fighter 2's HP is still above 0
     * @param fighter1Name  fighter 1's display name
     * @param fighter2Name  fighter 2's display name
     */
    public void recordTurnOutcome(boolean fighter1Alive, boolean fighter2Alive,
                                   String fighter1Name, String fighter2Name) {
        if (!fighter1Alive && !fighter2Alive) {
            state = MatchState.DRAW;
            winnerName = null;
        } else if (!fighter1Alive) {
            state = MatchState.VICTORY;
            winnerName = fighter2Name;
        } else if (!fighter2Alive) {
            state = MatchState.VICTORY;
            winnerName = fighter1Name;
        } else {
            state = MatchState.IN_PROGRESS;
        }
    }

    /**
     * Resets the match so a new one can be started. Specification: only
     * legal from VICTORY or DRAW.
     */
    public void reset() {
        if (state != MatchState.VICTORY && state != MatchState.DRAW) {
            throw new IllegalStateException("Match is not over yet");
        }
        state = MatchState.NOT_STARTED;
        winnerName = null;
    }
}
