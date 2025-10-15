package com.napier.sem;

import java.sql.*;
import java.util.ArrayList; // Required for ArrayList

public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connects to the MySQL database (with startup retry logic).
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait longer for the large database to finish loading (120 seconds)
                Thread.sleep(120000);

                // Connect to database using the Docker Compose service name 'db' and database 'world'
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");

                // Exit for loop if successful
                break;
            }
            catch (SQLException sqle)
            {
                // This is the expected failure while the database starts up
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnects from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Retrieves all countries in the world sorted by population (descending).
     * @return A list of Country objects, or null if there is an error.
     */
    public ArrayList<Country> getAllCountries()
    {
        if (con == null) {
            System.out.println("Database connection is null. Cannot retrieve data.");
            return null;
        }

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // SQL for Global Country Report (Largest to Smallest Population)
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Extract country information
            ArrayList<Country> countries = new ArrayList<>();
            while (rset.next())
            {
                Country country = new Country();
                country.Code = rset.getString("Code");
                country.Name = rset.getString("Name");
                country.Continent = rset.getString("Continent");
                country.Region = rset.getString("Region");
                // Use getLong() for large population numbers
                country.Population = rset.getLong("Population");
                country.Capital = rset.getString("Capital");
                countries.add(country);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println("Failed to get country details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Prints a list of countries in a formatted table.
     * @param countries The list of countries to print.
     */
    public void displayCountries(java.util.ArrayList<Country> countries)
    {
        if (countries == null)
        {
            System.out.println("No countries to display.");
            return;
        }

        // Print header: Code, Name, Continent, Region, Population, Capital
        System.out.println(String.format("%-6s %-45s %-20s %-20s %-15s %-20s",
                "Code", "Name", "Continent", "Region", "Population", "Capital"));
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

        // Loop over all countries in the list
        for (Country country : countries)
        {
            String country_string =
                    String.format("%-6s %-45s %-20s %-20s %-15s %-20s",
                            country.Code,
                            country.Name,
                            country.Continent,
                            country.Region,
                            country.Population,
                            country.Capital);
            System.out.println(country_string);
        }
    }

    /**
     * Application entry point for testing connection and data retrieval.
     */
    public static void main(String[] args)
    {
        App a = new App();
        a.connect();

        // Extract country information
        java.util.ArrayList<Country> countries = a.getAllCountries();

        // Display the results
        a.displayCountries(countries);

        a.disconnect();
    }
}
