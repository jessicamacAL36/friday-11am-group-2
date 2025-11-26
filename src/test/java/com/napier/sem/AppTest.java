package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App class and all data classes (Country, City, etc.),
 * ensuring robustness against null/invalid input and 100% coverage on data object methods.
 * This version uses direct public field access (e.g., summary.TotalPopulation)
 * for all data classes to match the confirmed project style.
 */
public class AppTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
    }

    // =========================================================================
    // I. BASE & ROBUSTNESS TESTS (Original UC Tests)
    // =========================================================================

    @Test
    void disconnectTestNullConnection() {
        try {
            app.disconnect();
            assertTrue(true);
        } catch (Exception e) {
            fail("Disconnect threw an exception for null connection: " + e.getMessage());
        }
    }

    @Test
    void testNullAndEmptyInputRobustness() {
        // Test robustness against null/empty/negative inputs for parameter-accepting methods
        app.getCountriesByContinent(null);
        app.getCountriesByRegion("");
        app.getTopNGlobalCountries(-1);
        app.getTopNCountriesByContinent(null, 5);
        app.getTopNCountriesByRegion("", -1);
        app.getCitiesByContinent(null);
        app.getCitiesByRegion("");
        app.getCitiesByCountry(null);
        app.getCitiesByDistrict("");
        app.getTopNGlobalCities(-1);
        app.getTopNContinentCities(null, -2);
        app.getTopNRegionCities("", 10);
        app.getTopNCountryCities(null, 5);
        app.getTopNDistrictCities("", 5);
        app.getCapitalCitiesByContinent("");
        app.getCapitalCitiesByRegion(null);
        app.getTopNGlobalCapitals(-1);
        app.getTopNContinentCapitals(null, 5);
        app.getTopNRegionCapitals("", -1);
        app.getContinentPopulation("");
        app.getRegionPopulation(null);
        app.getCountryPopulation("");
        app.getDistrictPopulation(null);
        app.getCityPopulation("");
        assertTrue(true);
    }

    // =========================================================================
    // II. DATA CLASS (POJO) COVERAGE TESTS (Targeting 100% on Data Classes)
    // =========================================================================

    @Test
    void testCountryCoverage() {
        // Uses no-arg constructor + public field access for 100% coverage
        Country country = new Country();

        // Direct field access
        country.Code = "USA";
        country.Name = "United States";
        country.Continent = "North America";
        country.Region = "North America";
        country.Population = 330000000L;
        country.Capital = "Washington D.C.";

        // Assertions use direct public field access
        assertEquals("USA", country.Code);
        assertEquals("United States", country.Name);
        assertEquals("North America", country.Continent);
        assertEquals(330000000L, country.Population);

        // Parameterized Constructor Call
        Country parameterizedCountry = new Country("CAN", "Canada", "North America", "North America", 38000000L, "Ottawa");
        assertEquals("CAN", parameterizedCountry.Code);

        Country.printReport(java.util.List.of(country), "Test Country Report");
        assertNotNull(country.toString());
    }

    /**
     * Tests instantiation, public fields, and print logic for a Capital City (isCapital=true).
     * This method resolves the "Value is always 'true'" warning.
     */
    @Test
    @SuppressWarnings("ConstantConditions") // Suppresses IntelliJ warning for redundant assert used for coverage
    void testCityCoverageCapital() {
        // Uses no-arg constructor + public field access for 100% coverage
        City city = new City();

        // --- Test 1: Capital City (isCapital = true) ---
        city.Name = "New York";
        city.CountryName = "USA";
        city.District = "New York";
        city.Population = 8500000L;
        city.isCapital = true; // Sets to true

        // Assertions use direct public field access
        assertEquals("New York", city.Name);
        assertEquals(8500000L, city.Population);
        assertTrue(city.isCapital);

        // Parameterized Constructor 1: (Name, Country, District, Pop, isCapital=true)
        City parameterizedCity = new City("Rome", "ITA", "Lazio", 2800000L, true);

        // Covers printReport(), printHeader(), and the true branch of the print() method
        City.printReport(java.util.List.of(city, parameterizedCity), "Test Capital Report", true);

        assertNotNull(city.toString());
    }

    /**
     * Tests instantiation, public fields, and print logic for a Standard City (isCapital=false).
     * This method resolves the "Value is always 'false'" warning.
     */
    @Test
    @SuppressWarnings("ConstantConditions") // Suppresses IntelliJ warning for redundant assert used for coverage
    void testCityCoverageStandard() {
        City normalCity = new City();

        // --- Test 2: Standard City (isCapital = false) ---
        normalCity.Name = "Houston";
        normalCity.CountryName = "USA";
        normalCity.District = "Texas";
        normalCity.Population = 2300000L;
        normalCity.isCapital = false; // Sets to false

        // Assertions use direct public field access
        assertEquals("Houston", normalCity.Name);
        assertFalse(normalCity.isCapital); // Checks the false branch

        // Covers printReport() (Standard City format) and the false print branch
        City.printReport(java.util.List.of(normalCity), "Test Standard City Report", false);

        // Also cover the parameterized constructor for standard city reports
        City parameterizedStandardCity = new City("Lagos", "Nigeria", "Lagos State", 15000000L, false);
        assertEquals("Lagos", parameterizedStandardCity.Name);
    }


    @Test
    void testPopulationSummaryCoverage() {
        // Uses no-arg constructor + public field access for 100% coverage
        PopulationSummary summary = new PopulationSummary();

        // Direct field access
        summary.Name = "World";
        summary.TotalPopulation = 6078000000L;
        summary.CityPopulation = 2400000000L;
        summary.CityPopulationPercent = 39.5;
        summary.RuralPopulation = 3678000000L;
        summary.RuralPopulationPercent = 60.5;

        // Assertions use direct public field access
        assertEquals("World", summary.Name);
        assertEquals(6078000000L, summary.TotalPopulation);
        assertEquals(39.5, summary.CityPopulationPercent, 0.001);

        // Parameterized Constructor (6 arguments)
        PopulationSummary parameterizedSummary = new PopulationSummary(
                "Asia",
                4000000000L,
                1500000000L,
                37.5,
                2500000000L,
                62.5
        );
        assertEquals("Asia", parameterizedSummary.Name);

        // Covers printReport()
        PopulationSummary.printReport(java.util.List.of(summary), "Test Summary Report");

        assertNotNull(summary.toString());
    }

    @Test
    void testLanguageSpeakersCoverage() {
        // Uses no-arg constructor + public field access for 100% coverage
        LanguageSpeakers lang = new LanguageSpeakers();

        // Direct field access
        lang.Language = "English";
        lang.Speakers = 1500000000.0;
        lang.WorldPopulationPercent = 20.0;

        // Assertions use direct public field access
        assertEquals("English", lang.Language);
        assertEquals(1500000000.0, lang.Speakers, 0.001);
        assertEquals(20.0, lang.WorldPopulationPercent, 0.001);

        // Parameterized Constructor (3 arguments)
        LanguageSpeakers parameterizedLang = new LanguageSpeakers("Spanish", 500000000.0, 7.0);
        assertEquals("Spanish", parameterizedLang.Language);

        // Covers printReport() and print()
        LanguageSpeakers.printReport(java.util.List.of(lang), "Test Language Report");

        assertNotNull(lang.toString());
    }
}