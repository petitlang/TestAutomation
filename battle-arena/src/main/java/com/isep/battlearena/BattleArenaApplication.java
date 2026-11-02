package com.isep.battlearena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Battle Arena demo application used in TD2
 * ("Applying Test Techniques"). Run it with the Maven Wrapper:
 *
 * <pre>
 *   ./mvnw spring-boot:run        (Linux / macOS)
 *   mvnw.cmd spring-boot:run      (Windows)
 * </pre>
 *
 * Then open http://localhost:8080 in a browser.
 */
@SpringBootApplication
public class BattleArenaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleArenaApplication.class, args);
    }
}
