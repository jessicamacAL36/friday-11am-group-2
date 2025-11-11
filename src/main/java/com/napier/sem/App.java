package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents the core application logic, database connection,
 * and methods for generating reports (countries, cities, population).
 */
public class App {
    /**
     * Connection to the MySQL database.
     */
    private Connection con = null;

    /**
     * Connects to the MySQL database.
     * Uses the Docker Compose service name 'db' as the host.
     */
    public void connect() {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found. Check dependencies in pom.xml.");
            return;
        }

        // Connection variables using the Docker service name 'db'
        // 'db' is the service name from docker-compose.yml
        String url = "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "example"; // Must match MYSQL_ROOT_PASSWORD in docker-compose.yml

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Attempting to connect to database... (Attempt " + (i + 1) + ")");
            try {
                // Wait a bit for the DB container to spin up
                Thread.sleep(3000);
                // Establish the connection
                con = DriverManager.getConnection(url, user, password);
                System.out.println("Successfully connected to MySQL database!");
                break; // Connection successful, exit the loop
            } catch (SQLException sqle) {
                System.out.println("SQL Exception: DB not yet available. Retrying...");
                // Print error details for debugging (optional)
                // sqle.printStackTrace();
            } catch (InterruptedException ie) {
                System.out.println("Connection interrupted.");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
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

    // --- Report Methods will go here, ready for your group's work ---
    // public void getCountryReportByPopulation() { ... }
}