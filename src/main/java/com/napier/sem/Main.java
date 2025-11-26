package com.napier.sem;

import java.util.Scanner;


/**
 * Main application class responsible for initialising the application and
 * connecting to the database, and running the interactive report menu.
 */
public class Main {
    /** Scanner for reading user input from the console. */
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Create an instance of the App class
        App app = new App();

        // FIX: Connect using command line arguments or default local Docker settings
        if(args.length < 2) {
            // Default: Connect to local Docker port 33060 with 30s delay
            app.connect("localhost:3306", 30000);
        } else {
            // GitHub Actions/Docker Compose: Use provided arguments
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        // Run the main menu loop if connection is successful
        if (app.getCon() != null) {
            runMenu(app);
        } else {
            System.err.println("Failed to start application: Database connection failed.");
        }

        // Disconnect from the database when done
        app.disconnect();
    }

    /**
     * Runs the main interactive menu loop.
     * @param app The application instance containing report methods.
     */
    private static void runMenu(App app) {
        int choice = -1;
        while (choice != 0) {
            displayMenu();
            try {
                // Read input as string first to handle mixed input (like '1' vs 'a')
                System.out.print("\nEnter choice: ");
                String input = scanner.nextLine().trim();

                // If input is empty, restart loop
                if (input.isEmpty()) continue;

                // Try to parse the input as an integer
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    // If parsing fails, treat it as an invalid non-numeric choice
                    choice = -1;
                }

                if (choice >= 1 && choice <= 32) {
                    executeReport(app, choice);
                } else if (choice == 99) {
                    app.runDemonstrationReports();
                } else if (choice == 0) {
                    System.out.println("Exiting application. Goodbye!");
                } else {
                    System.out.println("Invalid option. Please choose a number between 1 and 32, 99 for Demo, or 0 to Exit.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                // Using standard error output instead of printStackTrace() for cleaner logging:
                // e.printStackTrace();
            }
        }
    }

    /**
     * Displays the main menu options grouped by report category.
     */
    private static void displayMenu() {
        System.out.println("\n======================================================");
        System.out.println("         Population Reporting System - Main Menu");
        System.out.println("======================================================");
        System.out.println(" [1] Country Reports (UC01-UC06)");
        System.out.println(" [2] City Reports (UC07-UC16)");
        System.out.println(" [3] Capital City Reports (UC17-UC22)");
        System.out.println(" [4] Population Breakdown (UC23-UC25)");
        System.out.println(" [5] Aggregate Totals (UC26-UC31)");
        System.out.println(" [6] Language Report (UC32)");
        System.out.println("------------------------------------------------------");
        System.out.println(" [99] Run Demonstration Reports");
        System.out.println(" [0] Exit");
        System.out.println("======================================================");
        System.out.print("Enter category number (1-6) or direct UC number (1-32): ");
    }

    /**
     * Executes the appropriate report method based on the user's choice.
     * @param app The application instance.
     * @param choice The user's menu choice (1-32).
     */
    private static void executeReport(App app, int choice) {
        try {
            switch (choice) {
                // UC01-UC03: Global/Continent/Region (Requires no input/string input)
                case 1: app.getGlobalCountryReport(); break;
                case 2: app.getCountriesByContinent(getStringInput("Continent Name")); break;
                case 3: app.getCountriesByRegion(getStringInput("Region Name")); break;

                // UC04-UC06: Top N Countries (Requires N / String + N)
                case 4: app.getTopNGlobalCountries(getNInput()); break;
                case 5: app.getTopNCountriesByContinent(getStringInput("Continent Name"), getNInput()); break;
                case 6: app.getTopNCountriesByRegion(getStringInput("Region Name"), getNInput()); break;

                // UC07-UC11: Global/Continent/Region/Country/District Cities (String input)
                case 7: app.getGlobalCityReport(); break;
                case 8: app.getCitiesByContinent(getStringInput("Continent Name")); break;
                case 9: app.getCitiesByRegion(getStringInput("Region Name")); break;
                case 10: app.getCitiesByCountry(getStringInput("Country Name")); break;
                case 11: app.getCitiesByDistrict(getStringInput("District Name")); break;

                // UC12-UC16: Top N Cities (String + N input)
                case 12: app.getTopNGlobalCities(getNInput()); break;
                case 13: app.getTopNContinentCities(getStringInput("Continent Name"), getNInput()); break;
                case 14: app.getTopNRegionCities(getStringInput("Region Name"), getNInput()); break;
                case 15: app.getTopNCountryCities(getStringInput("Country Name"), getNInput()); break;
                case 16: app.getTopNDistrictCities(getStringInput("District Name"), getNInput()); break;

                // UC17-UC19: Capital Cities (String input)
                case 17: app.getGlobalCapitalCityReport(); break;
                case 18: app.getCapitalCitiesByContinent(getStringInput("Continent Name")); break;
                case 19: app.getCapitalCitiesByRegion(getStringInput("Region Name")); break;

                // UC20-UC22: Top N Capital Cities (String + N input)
                case 20: app.getTopNGlobalCapitals(getNInput()); break;
                case 21: app.getTopNContinentCapitals(getStringInput("Continent Name"), getNInput()); break;
                case 22: app.getTopNRegionCapitals(getStringInput("Region Name"), getNInput()); break;

                // UC23-UC25: Population Breakdown
                case 23: app.getPopulationDistributionByContinent(); break;
                case 24: app.getPopulationDistributionByRegion(); break;
                case 25: app.getPopulationDistributionByCountry(); break;

                // UC26-UC31: Aggregate Totals
                case 26: app.getWorldPopulation(); break;
                case 27: app.getContinentPopulation(getStringInput("Continent Name")); break;
                case 28: app.getRegionPopulation(getStringInput("Region Name")); break;
                case 29: app.getCountryPopulation(getStringInput("Country Name")); break;
                case 30: app.getDistrictPopulation(getStringInput("District Name")); break;
                case 31: app.getCityPopulation(getStringInput("City Name")); break;

                // UC32: Language Report
                case 32: app.getMajorLanguageSpeakers(); break;

                default: System.out.println("Error: Report number out of range (1-32).");
            }
        } catch (Exception e) {
            // FIX: Using robust logging via System.err instead of printStackTrace
            System.err.println("\n!!! Report Execution Error: Could not run report due to invalid input or database issue: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for a String input (e.g., Continent, Region, Country Name).
     * @param prompt The descriptive name of the required input.
     * @return The validated String input.
     */
    private static String getStringInput(String prompt) {
        System.out.print("Enter " + prompt + ": ");
        // Uses nextLine() because the scanner is already initialized and the previous nextInt() issues are handled in runMenu
        return scanner.nextLine().trim();
    }


    /**
     * Prompts the user for the integer value N (must be positive).
     * @return The validated integer N.
     */
    private static int getNInput() {
        int N = -1;
        while (N < 1) {
            try {
                System.out.print("Enter N (Top number to display, must be positive): ");

                // Read the whole line
                String input = scanner.nextLine().trim();

                // Check if input is empty
                if (input.isEmpty()) continue;

                // Attempt to parse the integer
                N = Integer.parseInt(input);

                if (N < 1) {
                    System.out.println("N must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for N.");
                N = -1; // Reset N to keep the loop running
            }
        }
        return N;
    }
}