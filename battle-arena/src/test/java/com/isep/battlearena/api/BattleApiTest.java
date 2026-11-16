package com.isep.battlearena.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.battlearena.ui.support.UiTestBase;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;

/**
 * STEP 8, TRACK A (optional): API tests with Playwright's APIRequestContext. No page is opened: the requests go
 * straight to the server, like JMeter, but written in Java and run by JUnit with the rest of your suite.
 *
 * The class extends UiTestBase only to reuse the application started on a free port and the clean state before
 * each test. (UiTestBase also launches a browser that this class does not use: see Q8.2.)
 *
 * To start: remove the @Disabled line below. The three helper methods are provided.
 */
@Disabled("Track A (optional): remove this line when you start")
class BattleApiTest extends UiTestBase {

    private final ObjectMapper json = new ObjectMapper();
    private APIRequestContext request;

    @BeforeEach
    void createRequestContext() {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl));
    }

    @AfterEach
    void disposeRequestContext() {
        request.dispose();
    }

    /** Same request as the setup page: a form posted to /start. Bram is always a Rogue with attack power 45. */
    private APIResponse start(String characterClass, int attackPower) {
        return request.post("/start", RequestOptions.create().setForm(FormData.create()
                .set("f1Name", "Aria").set("f1Class", characterClass).set("f1AttackPower", String.valueOf(attackPower))
                .set("f2Name", "Bram").set("f2Class", "ROGUE").set("f2AttackPower", "45")));
    }

    /** Plays one attack for the active fighter and returns the JSON answer. */
    private JsonNode attack(String attackType, boolean potion) throws IOException {
        APIResponse response = request.post("/api/attack", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData("{\"attackType\":\"" + attackType + "\",\"usePowerPotion\":" + potion + "}"));
        return json.readTree(response.text());
    }

    private JsonNode state() throws IOException {
        return json.readTree(request.get("/api/state").text());
    }

    @Test
    void aMatchCanBeStartedThroughTheApi() throws IOException {
        start("WARRIOR", 40);
        JsonNode state = state();
        assertEquals("IN_PROGRESS", state.get("matchState").asText());
        assertEquals("Aria", state.get("activeFighterName").asText());
        assertEquals(100, state.get("fighter2").get("hp").asInt());
    }

    @ParameterizedTest(name = "{0} / power {1} / {2} / potion={3}: {4} damage")
    @CsvSource({
            "MAGE,    50, FIREBALL,     true,  96",    // (30 + 50) x (1 + 0.10 + 0.10) = 96
            "RECRUIT, 50, POWER_STRIKE, false, 70"     // (20 + 50) x 1.00 = 70
            // TODO A.3: add three rows: ROGUE power 50 Quick Strike no potion; RECRUIT power 75 Quick Strike
            //   no potion; "mage" (lowercase) power 50 Fireball no potion. Compute each expected damage from the
            //   SPECIFICATION and write the calculation in a comment.
    })
    void damageThroughTheApiMatchesTheSpecification(String characterClass, int attackPower, String attackType,
                                                    boolean potion, int expectedDamage) throws IOException {
        // TODO A.2: start the match, play the attack, then check that lastResult.damage equals expectedDamage
        //   (assertEquals with a message that names the row, so that a failure is easy to read).
    }

    // TODO A.4: write a parameterized test with the values -10 and 101 (@CsvSource({"-10", "101"})).
    //   Specification: "Attack power is always a positive value between 1 and 100 (inclusive)."
    //   The form of the page blocks these values, the server must not rely on that: after the request to /start,
    //   the match must NOT have started (matchState NOT_STARTED).
}
