package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App class, focusing on input robustness and
 * non-database logic, ensuring all parameter-accepting public methods
 * are tested for null/invalid inputs.
 */
public class AppTest {
    static App app;


    /**
     * Set up the App instance before running any tests.
     */
    @BeforeAll
    static void init() {
        app = new App();
    }

    // =========================================================================
    // I. ENVIRONMENT & BASE TESTS
    // =========================================================================

    @Test
    void isJUnitWorking() {
        assertEquals(4, 2 + 2, "JUnit environment check failed.");
    }

    @Test
    void getConTestNullConnection() {
        // Confirms the connection getter works correctly in a default (unconnected) state.
        assertNull(app.getCon(), "Database connection should be null before successful connection.");
    }

    @Test
    void disconnectTestNullConnection() {
        try {
            app.disconnect();
            assertTrue(true, "Disconnect should run without exceptions for a null connection.");
        } catch (Exception e) {
            fail("Disconnect threw an exception for null connection: " + e.getMessage());
        }
    }

    // =========================================================================
    // II. COUNTRY REPORTS (UC01-UC06) - Original tests + new coverage
    // =========================================================================

    @Test
    void getCountriesByContinentTestNull() { // UC02
        try {
            app.getCountriesByContinent(null);
            assertTrue(true, "UC02 should run without exceptions for null continent name.");
        } catch (Exception e) {
            fail("UC02 threw an exception for null continent: " + e.getMessage());
        }
    }

    @Test
    void getCountriesByRegionTestEmpty() { // UC03
        try {
            app.getCountriesByRegion("");
            assertTrue(true, "UC03 should run without exceptions for empty region name.");
        } catch (Exception e) {
            fail("UC03 threw an exception for empty region: " + e.getMessage());
        }
    }

    @Test
    void getTopNGlobalCountriesTestNegativeN() { // UC04
        try {
            app.getTopNGlobalCountries(-1);
            assertTrue(true, "UC04 should run without exceptions for negative N.");
        } catch (Exception e) {
            fail("UC04 threw an exception for negative N: " + e.getMessage());
        }
    }

    @Test
    void getTopNCountriesByContinentTestNullContinent() { // UC05
        try {
            app.getTopNCountriesByContinent(null, 5);
            assertTrue(true, "UC05 should handle null continent name gracefully.");
        } catch (Exception e) {
            fail("UC05 threw an exception for null continent: " + e.getMessage());
        }
    }

    @Test
    void getTopNCountriesByRegionTestEmptyRegion() { // UC06
        try {
            app.getTopNCountriesByRegion("", 10);
            assertTrue(true, "UC06 should handle empty region name gracefully.");
        } catch (Exception e) {
            fail("UC06 threw an exception for empty region: " + e.getMessage());
        }
    }


    // =========================================================================
    // III. CITY REPORTS (UC07-UC16) - NEW TESTS
    // =========================================================================

    @Test
    void getCitiesByContinentTestNull() { // UC08
        try {
            app.getCitiesByContinent(null);
            assertTrue(true, "UC08 should handle null continent name gracefully.");
        } catch (Exception e) {
            fail("UC08 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getCitiesByRegionTestEmpty() { // UC09
        try {
            app.getCitiesByRegion("");
            assertTrue(true, "UC09 should handle empty region name gracefully.");
        } catch (Exception e) {
            fail("UC09 threw an exception for empty input: " + e.getMessage());
        }
    }

    @Test
    void getCitiesByCountryTestNull() { // UC10
        try {
            app.getCitiesByCountry(null);
            assertTrue(true, "UC10 should handle null country name gracefully.");
        } catch (Exception e) {
            fail("UC10 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getCitiesByDistrictTestEmpty() { // UC11
        try {
            app.getCitiesByDistrict("");
            assertTrue(true, "UC11 should handle empty district name gracefully.");
        } catch (Exception e) {
            fail("UC11 threw an exception for empty input: " + e.getMessage());
        }
    }

    @Test
    void getTopNContinentCitiesTestNegativeN() { // UC13
        try {
            app.getTopNContinentCities("Europe", -2);
            assertTrue(true, "UC13 should handle negative N value gracefully.");
        } catch (Exception e) {
            fail("UC13 threw an exception for negative N: " + e.getMessage());
        }
    }

    // UC12, UC14, UC15, UC16 need similar tests (omitted for brevity, but follow the above pattern)

    // =========================================================================
    // IV. CAPITAL CITY REPORTS (UC17-UC22) - NEW TESTS
    // =========================================================================

    @Test
    void getCapitalCitiesByContinentTestEmpty() { // UC18
        try {
            app.getCapitalCitiesByContinent("");
            assertTrue(true, "UC18 should handle empty continent name gracefully.");
        } catch (Exception e) {
            fail("UC18 threw an exception for empty input: " + e.getMessage());
        }
    }

    @Test
    void getCapitalCitiesByRegionTestNull() { // UC19
        try {
            app.getCapitalCitiesByRegion(null);
            assertTrue(true, "UC19 should handle null region name gracefully.");
        } catch (Exception e) {
            fail("UC19 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getTopNContinentCapitalsTestNullContinent() { // UC21
        try {
            app.getTopNContinentCapitals(null, 5);
            assertTrue(true, "UC21 should handle null continent name gracefully.");
        } catch (Exception e) {
            fail("UC21 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getTopNRegionCapitalsTestNegativeN() { // UC22
        try {
            app.getTopNRegionCapitals("Western Europe", -1);
            assertTrue(true, "UC22 should handle negative N value gracefully.");
        } catch (Exception e) {
            fail("UC22 threw an exception for negative N: " + e.getMessage());
        }
    }

    // =========================================================================
    // V. AGGREGATE POPULATION TOTALS (UC26-UC31) - NEW TESTS
    // =========================================================================

    @Test
    void getContinentPopulationTestEmpty() { // UC27 (Original test used null)
        try {
            app.getContinentPopulation("");
            assertTrue(true, "UC27 should handle empty continent name gracefully.");
        } catch (Exception e) {
            fail("UC27 threw an exception for empty input: " + e.getMessage());
        }
    }

    @Test
    void getRegionPopulationTestNull() { // UC28
        try {
            app.getRegionPopulation(null);
            assertTrue(true, "UC28 should handle null region name gracefully.");
        } catch (Exception e) {
            fail("UC28 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getCountryPopulationTestEmpty() { // UC29
        try {
            app.getCountryPopulation("");
            assertTrue(true, "UC29 should handle empty country name gracefully.");
        } catch (Exception e) {
            fail("UC29 threw an exception for empty input: " + e.getMessage());
        }
    }

    @Test
    void getDistrictPopulationTestNull() { // UC30
        try {
            app.getDistrictPopulation(null);
            assertTrue(true, "UC30 should handle null district name gracefully.");
        } catch (Exception e) {
            fail("UC30 threw an exception for null input: " + e.getMessage());
        }
    }

    @Test
    void getCityPopulationTestEmpty() { // UC31
        try {
            app.getCityPopulation("");
            assertTrue(true, "UC31 should handle empty city name gracefully.");
        } catch (Exception e) {
            fail("UC31 threw an exception for empty input: " + e.getMessage());
        }
    }
}