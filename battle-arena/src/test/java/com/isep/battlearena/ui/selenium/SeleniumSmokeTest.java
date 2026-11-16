package com.isep.battlearena.ui.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.isep.battlearena.ui.support.BattleArenaApi;

/**
 * STEP 6: the same kind of journey as your Playwright tests, written with Selenium WebDriver.
 * Selenium Manager (built into Selenium) downloads the right driver; Google Chrome must be installed.
 *
 * Main difference with Playwright: nothing waits for you. You write the wait yourself (explicit wait).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumSmokeTest {

    @Value("${local.server.port}") // port chosen at random when the application starts
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void startBrowser() {
        baseUrl = "http://localhost:" + port;
        new BattleArenaApi(baseUrl).ensureCleanState(); // same shared-state rule as the Playwright tests
        ChromeOptions options = new ChromeOptions();
        if (!"false".equals(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void theSetupPageIsDisplayed() {
        driver.get(baseUrl);
        assertEquals("Battle Arena", driver.findElement(By.cssSelector("h1")).getText());
        assertTrue(driver.findElement(By.cssSelector("button.btn-start")).isDisplayed());
    }

    @Test
    void aBattleCanBeStartedAndTheHitPointsAreUpdated() {
        // Default form values: (20 + 60) x 1.15 = 92 damage, so 100 - 92 = 8 HP left.
        driver.get(baseUrl);
        driver.findElement(By.cssSelector("button.btn-start")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // TODO 6.1: 1. wait until the URL contains "/battle"  (wait.until(ExpectedConditions.urlContains(...)))
        //           2. click the element with id "attack-power"
        //           3. wait until the text of the element with id "fighter2-hp-value" is "8"
        //              (ExpectedConditions.textToBe(By.id(...), "8"))
        //           4. check the text with assertEquals
        // Then count the lines you needed, compared with the Playwright version (BattleFlowTest, or your tutorial test).
    }
}
