package com.isep.battlearena.tutorial;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

class FirstStepsTest {

    @Test
    void theSetupPageIsDisplayed() {
        // Playwright controls the browser. This block closes it at the end.
        try (Playwright playwright = Playwright.create()) {
            // Start Chromium without a visible window.
            Browser browser = playwright.chromium().launch();
            // Use a fresh browser profile for this test.
            BrowserContext context = browser.newContext();
            // Open a tab.
            Page page = context.newPage();

            // Start the app by hand before running this test.
            page.navigate("http://localhost:8080");
            assertThat(page.locator("h1")).hasText("Battle Arena");

            browser.close();
        }
    }
}
