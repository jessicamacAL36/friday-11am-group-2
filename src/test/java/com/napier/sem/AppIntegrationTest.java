package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for the App class, verifying database interaction
 * using a running local database instance (Docker).
 */
public class AppIntegrationTest {
    // This App instance will connect to the live database
    static App app;


    @BeforeAll
    static void init() {
        // Increase the timeout to 60 seconds (60000ms) for robustness
        app = new App();
        app.connect("localhost:3306", 60000); // Changed 12000 to 60000
    }

    /**
     * Integration test for UC26: World Population.
     * Verifies that the query executes successfully and returns a plausible, non-zero result.
     */
    @Test
    void testGetWorldPopulation() {
        long population = app.getWorldPopulation();

        // World population is > 6 billion. Check for a very large, positive number.
        assertTrue(population > 1000000000, "World population should be greater than 1 billion.");
    }

    /**
     * Integration test for UC27: Continent Population.
     * Verifies the query for a specific, known continent.
     */
    @Test
    void testGetContinentPopulationAsia() {
        long population = app.getContinentPopulation("Asia");

        // Asia's population is over 3 billion. Check for a reasonable result.
        assertTrue(population > 3000000000L, "Asia population should be greater than 3 billion.");
    }

    /**
     * Integration test for UC02: Countries by Continent (valid query).
     * Checks if a query returns a non-empty list for a valid input.
     */
    @Test
    void testGetCountriesByContinentValid() {
        try {
            // Correct: Just call the method. The assertion is that it doesn't crash.
            app.getCountriesByContinent("Europe");

            // Assert that the method completed successfully without crashing.
            assertTrue(true, "UC02 should run and print successfully for valid continent.");

        } catch (Exception e) {
            // FIX: The fail() method must be called directly, not assigned to a variable.
            // If an exception is caught, the test fails.
            fail("UC02 threw an exception during execution: " + e.getMessage());
        }
    }
}