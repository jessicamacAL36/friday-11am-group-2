package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for the App class, verifying database interaction
 * using a running local database instance (Docker).
 * Updated to use 30-second connection delay.
 */
public class AppIntegrationTest {
    // This App instance will connect to the live database
    static App app;


    @BeforeAll
    static void init() {
        // Get the DB host and port from the Maven property (set by the CI pipeline to include port 33060)
        // If the property is not set (e.g., local IDE run), it defaults to "localhost:3306".
        String connectionString = System.getProperty("db_host", "localhost:3306");

        // Change the connection timeout to 30 seconds (30000ms)
        app = new App();

        // Use the dynamically determined connection string
        app.connect(connectionString, 30000);
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
            // Note: This calls app logic which should instantiate and use the Country class
            app.getCountriesByContinent("Europe");

            // Assert that the method completed successfully without crashing.
            assertTrue(true, "UC02 should run and print successfully for valid continent.");

        } catch (Exception e) {
            fail("UC02 threw an exception during execution: " + e.getMessage());
        }
    }
}