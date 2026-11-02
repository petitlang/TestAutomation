package com.isep.battlearena.model;

/**
 * The four states a Battle Arena match can be in. See
 * {@link com.isep.battlearena.service.MatchStateMachine} for the allowed
 * transitions between them.
 */
public enum MatchState {
    NOT_STARTED,
    IN_PROGRESS,
    VICTORY,
    DRAW
}
