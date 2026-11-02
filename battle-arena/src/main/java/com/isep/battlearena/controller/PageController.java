package com.isep.battlearena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.isep.battlearena.model.Fighter;
import com.isep.battlearena.model.MatchState;
import com.isep.battlearena.service.MatchStateMachine;

/**
 * Serves the two server-rendered (Thymeleaf) pages: the setup screen and
 * the battle screen. All the actual turn-by-turn play happens through
 * {@link BattleApiController}'s JSON endpoints, called by the JavaScript
 * animation layer - this class only ever renders a full page.
 *
 * Not in scope for TD2: this class has no business logic of its own to
 * test, it only wires the service package to the templates.
 */
@Controller
public class PageController {

    private final MatchStateMachine matchStateMachine;
    private final GameSession gameSession;

    public PageController(MatchStateMachine matchStateMachine, GameSession gameSession) {
        this.matchStateMachine = matchStateMachine;
        this.gameSession = gameSession;
    }

    @GetMapping("/")
    public String setup(Model model) {
        return "setup";
    }

    @PostMapping("/start")
    public String start(@RequestParam String f1Name, @RequestParam String f1Class,
                         @RequestParam int f1AttackPower, @RequestParam String f2Name,
                         @RequestParam String f2Class, @RequestParam int f2AttackPower) {
        Fighter fighter1 = new Fighter(f1Name, f1Class, f1AttackPower);
        Fighter fighter2 = new Fighter(f2Name, f2Class, f2AttackPower);
        gameSession.startNewMatch(fighter1, fighter2);
        return "redirect:/battle";
    }

    @GetMapping("/battle")
    public String battle(Model model) {
        if (matchStateMachine.getState() == MatchState.NOT_STARTED) {
            return "redirect:/";
        }
        model.addAttribute("fighter1", gameSession.getFighter1());
        model.addAttribute("fighter2", gameSession.getFighter2());
        return "battle";
    }
}
