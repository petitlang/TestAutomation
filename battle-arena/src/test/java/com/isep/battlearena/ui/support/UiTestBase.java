package com.isep.battlearena.ui.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

/**
 * Shared setup for UI tests, from the tutorial.
 * Starts the app on a free port and uses one browser per test class.
 * Each test gets a clean match, a fresh context and a saved trace.
 * Use -Dheadless=false to see the browser, or -Dslowmo=300 to slow it down.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class UiTestBase {

    @Value("${local.server.port}")
    private int port;

    protected static Playwright playwright;
    protected static Browser browser;

    protected String baseUrl;
    protected BattleArenaApi api;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(!"false".equals(System.getProperty("headless")))
                .setSlowMo(Double.parseDouble(System.getProperty("slowmo", "0"))));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        baseUrl = "http://localhost:" + port;
        api = new BattleArenaApi(baseUrl);
        api.ensureCleanState(); // Start each test from a clean match.
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true).setSnapshots(true).setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext(TestInfo info) throws IOException {
        String testClass = info.getTestClass().map(Class::getSimpleName).orElse("Test");
        String name = (testClass + "-" + info.getDisplayName()).replaceAll("[^A-Za-z0-9._-]+", "_");
        Path folder = Paths.get("target", "traces");
        Files.createDirectories(folder);
        context.tracing().stop(new Tracing.StopOptions().setPath(folder.resolve(name + ".zip")));
        context.close();
    }
}
