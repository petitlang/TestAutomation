package com.isep.battlearena.ui.support;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Small HTTP client for UI test setup, from the tutorial.
 * The app keeps one match. Reset is allowed only after victory or a draw.
 */
public class BattleArenaApi {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();
    private final String baseUrl;

    public BattleArenaApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public JsonNode state() {
        HttpResponse<String> response = send(HttpRequest.newBuilder(URI.create(baseUrl + "/api/state"))
                .GET().build());
        return parse(response.body());
    }

    public JsonNode attack(String attackType, boolean usePowerPotion) {
        String body = String.format("{\"attackType\":\"%s\",\"usePowerPotion\":%b}", attackType, usePowerPotion);
        HttpResponse<String> response = send(HttpRequest.newBuilder(URI.create(baseUrl + "/api/attack"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build());
        return parse(response.body());
    }

    /** Start through the form endpoint. Bram stays Rogue with power 45. */
    public void start(String fighter1Class, int fighter1AttackPower) {
        String form = "f1Name=Aria&f1Class=" + fighter1Class + "&f1AttackPower=" + fighter1AttackPower
                + "&f2Name=Bram&f2Class=ROGUE&f2AttackPower=45";
        HttpResponse<String> response = send(HttpRequest.newBuilder(URI.create(baseUrl + "/start"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form)).build());
        if (response.statusCode() >= 400) {
            throw new IllegalStateException("Start refused, HTTP status " + response.statusCode());
        }
    }

    /** Finish the current match if needed, then reset it. */
    public void ensureCleanState() {
        for (int i = 0; i < 50; i++) {
            String matchState = state().get("matchState").asText();
            if (matchState.equals("NOT_STARTED")) {
                return;
            }
            if (matchState.equals("IN_PROGRESS")) {
                attack("QUICK_STRIKE", false); // Costs no mana.
            } else { // VICTORY or DRAW
                HttpResponse<String> response = send(HttpRequest.newBuilder(URI.create(baseUrl + "/api/reset"))
                        .POST(HttpRequest.BodyPublishers.noBody()).build());
                if (response.statusCode() != 200) {
                    throw new IllegalStateException("Reset impossible, HTTP status " + response.statusCode());
                }
            }
        }
        throw new IllegalStateException("Could not put Battle Arena back in a clean state");
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IllegalStateException("Battle Arena is not reachable at " + baseUrl, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while calling Battle Arena", e);
        }
    }

    private JsonNode parse(String body) {
        try {
            return json.readTree(body);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected response from Battle Arena: " + body, e);
        }
    }
}
