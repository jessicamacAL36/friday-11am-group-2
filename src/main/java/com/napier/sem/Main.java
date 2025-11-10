package com.napier.sem;

/**
 * Main application class responsible for initialising the application and
 * connecting to the database.
 */
public class Main {
    public static void main(String[] args) {
        // Create an instance of the App class
        App app = new App();

        // Connect to the database
        app.connect();

        // --- Execute Reports Here ---
        // Example: app.getCountryReportByPopulation();

        // Disconnect from the database when done
        app.disconnect();
    }
}