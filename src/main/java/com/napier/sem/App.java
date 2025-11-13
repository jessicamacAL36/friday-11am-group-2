package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

/**
 * Represents the core application logic, database connection,
 * and methods for generating all 32 required reports (countries, cities, population).
 */
@SuppressWarnings({"unused"})
public class App {
    /** Connection to the MySQL database. */
    private Connection con = null;

    // --- Functional Interface for mapping ResultSet to objects ---
    @FunctionalInterface
    private interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /**
     * Connects to the MySQL database at the specified location after a delay.
     * @param location The hostname and port (e.g., "localhost:33060").
     * @param delay The initial delay in milliseconds before the first connection attempt.
     */
    public void connect(String location, int delay) { // <-- UPDATED SIGNATURE
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found. Check dependencies in pom.xml.");
            return;
        }

        String user = "root";
        String password = "example";

        // --- 1. INITIAL LONG WAIT (HAPPENS ONLY ONCE) ---
        try {
            System.out.println("Initial connection delay: waiting " + delay + "ms...");
            Thread.sleep(delay); // Apply the full, single long delay here.
        } catch (InterruptedException ie) {
            System.out.println("Connection interrupted during initial wait.");
            Thread.currentThread().interrupt();
            return;
        }

        // --- 2. RETRY LOOP (HAPPENS MULTIPLE TIMES WITH SHORT DELAY) ---
        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Attempting to connect to database... (Attempt " + (i + 1) + ")");
            try {
                // Connect to database using location parameter and 'world' database name
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        user, password);
                System.out.println("Successfully connected to MySQL database!");
                return; // Exit method on success
            } catch (SQLException sqle) {
                System.out.println("SQL Exception: DB not yet available. Retrying in 3s...");
                System.out.println(sqle.getMessage());
                try {
                    // Short, standard delay for retries (3 seconds)
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    System.out.println("Retry sleep interrupted.");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        System.err.println("Failed to connect to database after all retries.");
    }

    /**
     * Disconnects from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                System.err.println("Error closing connection to database: " + e.getMessage());
            }
        }
    }

    /**
     * Getter for the database connection.
     * @return The Connection object.
     */
    public Connection getCon() {
        return con;
    }

    /**
     * Executes a generic SQL query and maps the results using a provided mapper.
     */
    private <T> List<T> executeReportQuery(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        if (con == null) return results;

        try (PreparedStatement pStmt = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String) {
                    pStmt.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Integer) {
                    pStmt.setInt(i + 1, (int) params[i]);
                }
            }
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error executing report: " + e.getMessage());
        }
        return results;
    }

    // =========================================================================
    // I. Country Report Methods (UC01 - UC06)
    // =========================================================================

    private Country mapToCountry(ResultSet rs) throws SQLException {
        return new Country(
                rs.getString("Code"),
                rs.getString("Name"),
                rs.getString("Continent"),
                rs.getString("Region"),
                rs.getLong("Population"),
                rs.getString("Capital")
        );
    }

    // UC01: All countries in the world
    public void getGlobalCountryReport() {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID ORDER BY c.Population DESC";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry);
        Country.printReport(countries, "UC01: All Countries in the World");
    }

    // UC02: All countries in a continent
    public void getCountriesByContinent(String continentName) {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID " +
                "WHERE c.Continent = ? ORDER BY c.Population DESC";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry, continentName);
        Country.printReport(countries, "UC02: All Countries in Continent '" + continentName + "'");
    }

    // UC03: All countries in a region
    public void getCountriesByRegion(String regionName) {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID " +
                "WHERE c.Region = ? ORDER BY c.Population DESC";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry, regionName);
        Country.printReport(countries, "UC03: All Countries in Region '" + regionName + "'");
    }

    // UC04: Top N populated countries in the world
    public void getTopNGlobalCountries(int N) {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID ORDER BY c.Population DESC LIMIT ?";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry, N);
        Country.printReport(countries, "UC04: Top " + N + " Populated Countries in the World");
    }

    // UC05: Top N populated countries in a continent
    public void getTopNCountriesByContinent(String continentName, int N) {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID " +
                "WHERE c.Continent = ? ORDER BY c.Population DESC LIMIT ?";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry, continentName, N);
        Country.printReport(countries, "UC05: Top " + N + " Populated Countries in Continent '" + continentName + "'");
    }

    // UC06: Top N populated countries in a region
    public void getTopNCountriesByRegion(String regionName, int N) {
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                "FROM country AS c JOIN city AS cap ON c.Capital = cap.ID " +
                "WHERE c.Region = ? ORDER BY c.Population DESC LIMIT ?";
        List<Country> countries = executeReportQuery(sql, this::mapToCountry, regionName, N);
        Country.printReport(countries, "UC06: Top " + N + " Populated Countries in Region '" + regionName + "'");
    }

    // =========================================================================
    // II. City Report Methods (UC07 - UC16)
    // =========================================================================

    private List<City> executeCityReportQuery(String sql, boolean isCapital, Object... params) {
        return executeReportQuery(sql, (rs) -> new City(
                rs.getString("Name"),
                rs.getString("Country"),
                rs.getString("District"),
                rs.getLong("Population"),
                isCapital
        ), params);
    }

    // UC07: All cities in the world
    public void getGlobalCityReport() {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, false);
        City.printReport(cities, "UC07: All Cities in the World", false);
    }

    // UC08: All cities in a continent
    public void getCitiesByContinent(String continentName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Continent = ? ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, false, continentName);
        City.printReport(cities, "UC08: All Cities in Continent '" + continentName + "'", false);
    }

    // UC09: All cities in a region
    public void getCitiesByRegion(String regionName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Region = ? ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, false, regionName);
        City.printReport(cities, "UC09: All Cities in Region '" + regionName + "'", false);
    }

    // UC10: All cities in a country
    public void getCitiesByCountry(String countryName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Name = ? ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, false, countryName);
        City.printReport(cities, "UC10: All Cities in Country '" + countryName + "'", false);
    }

    // UC11: All cities in a district
    public void getCitiesByDistrict(String districtName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE ci.District = ? ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, false, districtName);
        City.printReport(cities, "UC11: All Cities in District '" + districtName + "'", false);
    }

    // UC12: Top N populated cities in the world
    public void getTopNGlobalCities(int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code ORDER BY ci.Population DESC LIMIT ?";
        List<City> cities = executeCityReportQuery(sql, false, N);
        City.printReport(cities, "UC12: Top " + N + " Populated Cities in the World", false);
    }

    // UC13: Top N populated cities in a continent
    public void getTopNContinentCities(String continentName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Continent = ? ORDER BY ci.Population DESC LIMIT ?";
        List<City> cities = executeCityReportQuery(sql, false, continentName, N);
        City.printReport(cities, "UC13: Top " + N + " Populated Cities in Continent '" + continentName + "'", false);
    }

    // UC14: Top N populated cities in a region
    public void getTopNRegionCities(String regionName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Region = ? ORDER BY ci.Population DESC LIMIT ?";
        List<City> cities = executeCityReportQuery(sql, false, regionName, N);
        City.printReport(cities, "UC14: Top " + N + " Populated Cities in Region '" + regionName + "'", false);
    }

    // UC15: Top N populated cities in a country
    public void getTopNCountryCities(String countryName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE c.Name = ? ORDER BY ci.Population DESC LIMIT ?";
        List<City> cities = executeCityReportQuery(sql, false, countryName, N);
        City.printReport(cities, "UC15: Top " + N + " Populated Cities in Country '" + countryName + "'", false);
    }

    // UC16: Top N populated cities in a district
    public void getTopNDistrictCities(String districtName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.District, ci.Population " +
                "FROM city AS ci JOIN country AS c ON ci.CountryCode = c.Code " +
                "WHERE ci.District = ? ORDER BY ci.Population DESC LIMIT ?";
        List<City> cities = executeCityReportQuery(sql, false, districtName, N);
        City.printReport(cities, "UC16: Top " + N + " Populated Cities in District '" + districtName + "'", false);
    }

    // =========================================================================
    // III. Capital City Reports (UC17 - UC22)
    // =========================================================================


    // UC17: All capital cities in the world
    public void getGlobalCapitalCityReport() {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, true);
        City.printReport(cities, "UC17: All Capital Cities in the World", true);
    }

    // UC18: All capital cities in a continent
    public void getCapitalCitiesByContinent(String continentName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID WHERE c.Continent = ? ORDER BY ci.Population DESC";
        List<City> cities = executeCityReportQuery(sql, true, continentName);
        City.printReport(cities, "UC18: All Capital Cities in Continent '" + continentName + "'", true);
    }

    // UC19: All capital cities in a region
    public void getCapitalCitiesByRegion(String regionName) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID WHERE c.Region = ? ORDER BY ci.Population DESC";
        // FIX: Must call executeCityReportQuery
        List<City> cities = executeCityReportQuery(sql, true, regionName);
        City.printReport(cities, "UC19: All Capital Cities in Region '" + regionName + "'", true);
    }

    // UC20: Top N populated capital cities in the world (around line 322)
    public void getTopNGlobalCapitals(int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID ORDER BY ci.Population DESC LIMIT ?";
        // FIX: The parameters must be SQL, boolean (true for capital), and N
        List<City> cities = executeCityReportQuery(sql, true, N);
        City.printReport(cities, "UC20: Top " + N + " Populated Capital Cities in the World", true);
    }

    // UC21: Top N populated capital cities in a continent
    public void getTopNContinentCapitals(String continentName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID WHERE c.Continent = ? ORDER BY ci.Population DESC LIMIT ?";

        // FIX: Change executeReportQuery to executeCityReportQuery
        List<City> cities = executeCityReportQuery(sql, true, continentName, N); // <-- THIS IS THE FIX

        City.printReport(cities, "UC21: Top " + N + " Populated Capital Cities in Continent '" + continentName + "'", true);
    }

    // UC22: Top N populated capital cities in a region
    public void getTopNRegionCapitals(String regionName, int N) {
        String sql = "SELECT ci.Name, c.Name AS Country, ci.Population, ci.District FROM country AS c " +
                "JOIN city AS ci ON c.Capital = ci.ID WHERE c.Region = ? ORDER BY ci.Population DESC LIMIT ?";
        // FIX: Must call executeCityReportQuery
        List<City> cities = executeCityReportQuery(sql, true, regionName, N);
        City.printReport(cities, "UC22: Top " + N + " Populated Capital Cities in Region '" + regionName + "'", true);
    }

    // =========================================================================
    // IV. Population Distribution Reports (UC23 - UC25)
    // =========================================================================

    /**
     * Executes a population distribution query and prints the report.
     * Note: Changed return type to void to resolve "Return value of the method is never used" warnings.
     */
    private void executePopulationSummaryQuery(String groupColumn, String titlePrefix, String sql) {
        List<PopulationSummary> summaries = new ArrayList<>();
        if (con == null) return;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                summaries.add(new PopulationSummary(
                        rs.getString("Name"),
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation"),
                        rs.getDouble("CityPopulationPercent"),
                        rs.getLong("RuralPopulation"),
                        rs.getDouble("RuralPopulationPercent")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error executing population distribution report (" + titlePrefix + "): " + e.getMessage());
        }
        PopulationSummary.printReport(summaries, titlePrefix + " Population Breakdown");
    }

    // UC23: Continent Population Breakdown
    public void getPopulationDistributionByContinent() {
        String sql = "SELECT c.Continent AS Name, " +
                "SUM(c.Population) AS TotalPopulation, " +
                "SUM(ci.CityPopulation) AS CityPopulation, " +
                "SUM(c.Population) - SUM(ci.CityPopulation) AS RuralPopulation, " +
                "CAST(SUM(ci.CityPopulation) / SUM(c.Population) * 100 AS DECIMAL(5,2)) AS CityPopulationPercent, " +
                "CAST(((SUM(c.Population) - SUM(ci.CityPopulation)) / SUM(c.Population) * 100) AS DECIMAL(5,2)) AS RuralPopulationPercent " +
                "FROM country AS c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS CityPopulation FROM city GROUP BY CountryCode) AS ci " +
                "ON c.Code = ci.CountryCode " +
                "GROUP BY c.Continent " +
                "ORDER BY TotalPopulation DESC";
        executePopulationSummaryQuery("c.Continent", "UC23: Continent", sql);
    }

    // UC24: Region Population Breakdown
    public void getPopulationDistributionByRegion() {
        String sql = "SELECT c.Region AS Name, " +
                "SUM(c.Population) AS TotalPopulation, " +
                "SUM(ci.CityPopulation) AS CityPopulation, " +
                "SUM(c.Population) - SUM(ci.CityPopulation) AS RuralPopulation, " +
                "CAST(SUM(ci.CityPopulation) / SUM(c.Population) * 100 AS DECIMAL(5,2)) AS CityPopulationPercent, " +
                "CAST(((SUM(c.Population) - SUM(ci.CityPopulation)) / SUM(c.Population) * 100) AS DECIMAL(5,2)) AS RuralPopulationPercent " +
                "FROM country AS c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS CityPopulation FROM city GROUP BY CountryCode) AS ci " +
                "ON c.Code = ci.CountryCode " +
                "GROUP BY c.Region " +
                "ORDER BY TotalPopulation DESC";
        executePopulationSummaryQuery("c.Region", "UC24: Region", sql);
    }

    // UC25: Country Population Breakdown
    public void getPopulationDistributionByCountry() {
        String sql = "SELECT c.Name AS Name, " +
                "c.Population AS TotalPopulation, " +
                "SUM(ci.Population) AS CityPopulation, " +
                "c.Population - SUM(ci.Population) AS RuralPopulation, " +
                "CAST(SUM(ci.Population) / c.Population * 100 AS DECIMAL(5,2)) AS CityPopulationPercent, " +
                "CAST(((c.Population - SUM(ci.Population)) / c.Population * 100) AS DECIMAL(5,2)) AS RuralPopulationPercent " +
                "FROM country AS c " +
                "LEFT JOIN city AS ci ON c.Code = ci.CountryCode " +
                "GROUP BY c.Code, c.Name, c.Population " +
                "ORDER BY c.Population DESC";
        executePopulationSummaryQuery("c.Name", "UC25: Country", sql);
    }

    // =========================================================================
    // V. Aggregate Population Totals (UC26 - UC31)
    // =========================================================================

    /**
     * Helper method for single aggregate population results (UC26-UC31).
     */
    private long executeSinglePopulationQuery(String sql, String parameter, String ucNumber, String entityName) {
        long population = 0;
        if (con == null) return population;
        String title = String.format("UC%s: %s Population%s", ucNumber, entityName, parameter != null ? " (" + parameter + ")" : "");

        try (PreparedStatement pStmt = con.prepareStatement(sql)) {
            if (parameter != null) {
                pStmt.setString(1, parameter);
            }
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    population = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error for " + title + ": " + e.getMessage());
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        System.out.println("\n=======================================================");
        System.out.println(title);
        System.out.println("Result: " + (population > 0 ? nf.format(population) : "Data Not Available"));
        System.out.println("=======================================================");
        return population;
    }

    // UC26: World Population
    public long getWorldPopulation() {
        return executeSinglePopulationQuery("SELECT SUM(Population) FROM country", null, "26", "World");
    }

    // UC27: Continent Population
    public long getContinentPopulation(String continentName) {
        return executeSinglePopulationQuery("SELECT SUM(Population) FROM country WHERE Continent = ?", continentName, "27", "Continent");
    }

    // UC28: Region Population
    public long getRegionPopulation(String regionName) {
        return executeSinglePopulationQuery("SELECT SUM(Population) FROM country WHERE Region = ?", regionName, "28", "Region");
    }

    // UC29: Country Population
    public long getCountryPopulation(String countryName) {
        return executeSinglePopulationQuery("SELECT Population FROM country WHERE Name = ?", countryName, "29", "Country");
    }

    // UC30: District Population
    public long getDistrictPopulation(String districtName) {
        return executeSinglePopulationQuery("SELECT SUM(Population) FROM city WHERE District = ?", districtName, "30", "District");
    }

    // UC31: City Population
    public long getCityPopulation(String cityName) {
        return executeSinglePopulationQuery("SELECT Population FROM city WHERE Name = ?", cityName, "31", "City");
    }

    // =========================================================================
    // VI. Language Report (UC32)
    // =========================================================================

    // UC32: Global Language Speakers Report
    public void getMajorLanguageSpeakers() {
        if (con == null) return;
        String sql = "SELECT cl.Language, " +
                "SUM(c.Population * (cl.Percentage / 100)) AS Speakers, " +
                "(SUM(c.Population * (cl.Percentage / 100)) / (SELECT SUM(Population) FROM country) * 100) AS WorldPopulationPercent " +
                "FROM countrylanguage AS cl " +
                "JOIN country AS c ON cl.CountryCode = c.Code " +
                "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                "GROUP BY cl.Language " +
                "ORDER BY Speakers DESC";

        List<LanguageSpeakers> speakers = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                speakers.add(new LanguageSpeakers(
                        rs.getString("Language"),
                        rs.getDouble("Speakers"),
                        rs.getDouble("WorldPopulationPercent")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error executing language report (UC32): " + e.getMessage());
        }
        LanguageSpeakers.printReport(speakers, "UC32: Global Language Speakers Report (Chinese, English, Hindi, Spanish, Arabic)");
    }


    // =========================================================================
    // VII. Demonstration Method
    // =========================================================================

    /**
     * Runs a selection of reports for demonstration purposes.
     */
    public void runDemonstrationReports() {
        // --- Demonstration Inputs ---
        String continent = "Asia";
        String region = "Eastern Asia";
        String country = "United Kingdom";
        String district = "Noord-Holland";
        String city = "London";
        int N = 5;

        System.out.println("\n\n#####################################################");
        System.out.println("# Running Demonstration Reports #");
        System.out.println("#####################################################");

        // A. Country Reports
        getGlobalCountryReport(); // UC01
        getTopNCountriesByContinent(continent, N); // UC05

        // B. City Reports
        getGlobalCityReport(); // UC07
        getCitiesByDistrict("California"); // UC11

        // C. Capital City Reports
        getTopNGlobalCapitals(N); // UC20

        // D. Population Breakdown Reports
        getPopulationDistributionByContinent(); // UC23



        // E. Single Population Totals
        // Declaring local variables to capture the return values resolves the "return value of the method is never used" warnings.
        long worldPop = getWorldPopulation(); // UC26
        long continentPop = getContinentPopulation(continent); // UC27
        long regionPop = getRegionPopulation(region); // UC28
        long countryPop = getCountryPopulation(country); // UC29
        long districtPop = getDistrictPopulation(district); // UC30
        long cityPop = getCityPopulation(city); // UC31

        // F. Language Report
        getMajorLanguageSpeakers(); // UC32
    }
}