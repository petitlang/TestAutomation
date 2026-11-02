/**
 * Battle Arena - presentation layer.
 *
 * This script owns ZERO game rules. It only:
 *   1. asks the server what the current state is (GET /api/state),
 *   2. tells the server which attack button was clicked (POST /api/attack),
 *   3. animates whatever numbers the server sends back.
 *
 * All damage math, class bonuses, critical hits and match-state rules
 * live in the Java service package (CombatService, MatchStateMachine).
 * This file must never be extended to compute damage or decide who won:
 * that duplication is exactly what would make the server hard to test
 * and the client easy to cheat.
 */
(function () {
    'use strict';

    const attackButtons = document.querySelectorAll('.btn-attack');
    const potionCheckbox = document.getElementById('use-potion-checkbox');
    const resetButton = document.getElementById('reset-button');
    const banner = document.getElementById('match-banner');
    const activeIndicator = document.getElementById('active-fighter-indicator');
    const log = document.getElementById('battle-log');

    const panels = {
        1: {
            panel: document.getElementById('fighter1-panel'),
            hpFill: document.getElementById('fighter1-hp-fill'),
            hpValue: document.getElementById('fighter1-hp-value'),
            manaFill: document.getElementById('fighter1-mana-fill'),
            manaValue: document.getElementById('fighter1-mana-value'),
            name: document.getElementById('fighter1-name').textContent
        },
        2: {
            panel: document.getElementById('fighter2-panel'),
            hpFill: document.getElementById('fighter2-hp-fill'),
            hpValue: document.getElementById('fighter2-hp-value'),
            manaFill: document.getElementById('fighter2-mana-fill'),
            manaValue: document.getElementById('fighter2-mana-value'),
            name: document.getElementById('fighter2-name').textContent
        }
    };

    function panelFor(fighterName) {
        return panels[1].name === fighterName ? panels[1] : panels[2];
    }

    function updateBars(fighter1, fighter2) {
        panels[1].hpFill.style.width = fighter1.hp + '%';
        panels[1].hpValue.textContent = fighter1.hp;
        panels[1].manaFill.style.width = fighter1.mana + '%';
        panels[1].manaValue.textContent = fighter1.mana;

        panels[2].hpFill.style.width = fighter2.hp + '%';
        panels[2].hpValue.textContent = fighter2.hp;
        panels[2].manaFill.style.width = fighter2.mana + '%';
        panels[2].manaValue.textContent = fighter2.mana;
    }

    function showDamagePopup(fighterPanel, amount, critical) {
        const popup = document.createElement('div');
        popup.className = 'damage-popup' + (critical ? ' critical' : '');
        popup.textContent = (amount < 0 ? '+' + Math.abs(amount) : '-' + amount) +
            (critical ? '!' : '');
        fighterPanel.panel.appendChild(popup);
        fighterPanel.panel.classList.add('shake');
        setTimeout(() => popup.remove(), 900);
        setTimeout(() => fighterPanel.panel.classList.remove('shake'), 400);
    }

    function appendLog(message) {
        const p = document.createElement('p');
        p.textContent = message;
        log.appendChild(p);
        log.scrollTop = log.scrollHeight;
    }

    function setControlsEnabled(enabled) {
        attackButtons.forEach((btn) => (btn.disabled = !enabled));
    }

    function render(state) {
        updateBars(state.fighter1, state.fighter2);

        if (state.lastResult && state.lastResult.attackPerformed) {
            const defenderPanel = panelFor(state.lastResult.defenderName);
            showDamagePopup(defenderPanel, state.lastResult.damage, state.lastResult.criticalHit);
            appendLog(
                state.lastResult.attackerName + ' hits ' + state.lastResult.defenderName +
                ' for ' + state.lastResult.damage + ' damage' +
                (state.lastResult.criticalHit ? ' (CRITICAL!)' : '') + '.'
            );
        } else if (state.lastResult && !state.lastResult.attackPerformed) {
            appendLog(state.lastResult.attackerName + ' does not have enough mana for that attack.');
        }

        if (state.matchState === 'IN_PROGRESS') {
            banner.classList.add('hidden');
            resetButton.classList.add('hidden');
            setControlsEnabled(true);
            activeIndicator.textContent = state.activeFighterName + "'s turn";
        } else if (state.matchState === 'VICTORY') {
            banner.textContent = state.winnerName + ' wins the battle!';
            banner.classList.remove('hidden');
            resetButton.classList.remove('hidden');
            setControlsEnabled(false);
            activeIndicator.textContent = '';
        } else if (state.matchState === 'DRAW') {
            banner.textContent = 'Both fighters fall - it is a draw!';
            banner.classList.remove('hidden');
            resetButton.classList.remove('hidden');
            setControlsEnabled(false);
            activeIndicator.textContent = '';
        }
    }

    async function fetchState() {
        const response = await fetch('/api/state');
        const state = await response.json();
        render(state);
    }

    async function sendAttack(attackType) {
        setControlsEnabled(false);
        try {
            const response = await fetch('/api/attack', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    attackType: attackType,
                    usePowerPotion: potionCheckbox.checked
                })
            });
            const state = await response.json();
            potionCheckbox.checked = false;
            render(state);
        } finally {
            setControlsEnabled(true);
        }
    }

    attackButtons.forEach((button) => {
        button.addEventListener('click', () => sendAttack(button.dataset.attackType));
    });

    resetButton.addEventListener('click', async () => {
        await fetch('/api/reset', { method: 'POST' });
        window.location.href = '/';
    });

    fetchState();
})();
