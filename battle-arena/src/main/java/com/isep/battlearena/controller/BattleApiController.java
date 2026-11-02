package com.isep.battlearena.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.isep.battlearena.model.AttackResult;
import com.isep.battlearena.model.AttackType;
import com.isep.battlearena.model.Fighter;
import com.isep.battlearena.model.MatchState;
import com.isep.battlearena.service.CombatService;
import com.isep.battlearena.service.MatchStateMachine;

/**
 * The JSON API the JavaScript animation layer calls for every attack.
 * This class is intentionally thin: it looks up who is attacking whom,
 * hands the attack to {@link CombatService}, updates
 * {@link MatchStateMachine} from the result, and reports back a snapshot.
 * It never computes damage or decides the match outcome itself - all of
 * that lives in the service package, which is what TD2 asks students to
 * test. This controller is not in scope for the lab.
 */
@RestController
public class BattleApiController {

    private final CombatService combatService;
    private final MatchStateMachine matchStateMachine;
    private final GameSession gameSession;

    public BattleApiController(CombatService combatService, MatchStateMachine matchStateMachine,
                                GameSession gameSession) {
        this.combatService = combatService;
        this.matchStateMachine = matchStateMachine;
        this.gameSession = gameSession;
    }

    @GetMapping("/api/state")
    public BattleStateView state() {
        return currentView(null);
    }

    @PostMapping("/api/attack")
    public BattleStateView attack(@RequestBody AttackRequest request) {
        if (matchStateMachine.getState() != MatchState.IN_PROGRESS) {
            return currentView(AttackResult.failure("N/A", "MATCH_NOT_IN_PROGRESS"));
        }

        Fighter attacker = gameSession.getActiveFighter();
        Fighter defender = gameSession.getWaitingFighter();
        AttackType attackType = AttackType.valueOf(request.getAttackType());

        AttackResult result = combatService.resolveAttack(attacker, defender, attackType,
                request.isUsePowerPotion());

        if (result.isAttackPerformed()) {
            Fighter fighter1 = gameSession.getFighter1();
            Fighter fighter2 = gameSession.getFighter2();
            matchStateMachine.recordTurnOutcome(fighter1.isAlive(), fighter2.isAlive(),
                    fighter1.getName(), fighter2.getName());

            if (matchStateMachine.getState() == MatchState.IN_PROGRESS) {
                gameSession.switchActiveFighter();
            }
        }

        return currentView(result);
    }

    @PostMapping("/api/reset")
    public BattleStateView reset() {
        gameSession.resetMatch();
        return currentView(null);
    }

    private BattleStateView currentView(AttackResult lastResult) {
        Fighter fighter1 = gameSession.getFighter1();
        Fighter fighter2 = gameSession.getFighter2();
        String activeName = matchStateMachine.getState() == MatchState.IN_PROGRESS
                ? gameSession.getActiveFighter().getName()
                : null;
        return new BattleStateView(lastResult, fighter1, fighter2, activeName,
                matchStateMachine.getState(), matchStateMachine.getWinnerName());
    }
}
