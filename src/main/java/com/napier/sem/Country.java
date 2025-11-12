package com.napier.sem;

import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

/**
 * Represents a country record from the world database,
 * including population reporting and formatting logic.
 */
public class Country {
    /** Country Code (Primary Key) */
    public String Code;
    /** Country Name */
    public String Name;
    /** Continent Name */
    public String Continent;
    /** Region Name */
    public String Region;
    /** Total Population */
    public long Population;
    /** Capital City Name */
    public String Capital;

    /**
     * Constructor for Country object.
     */
    public Country(String code, String name, String continent, String region, long population, String capital) {
        this.Code = code;
        this.Name = name;
        this.Continent = continent;
        this.Region = region;
        this.Population = population;
        this.Capital = capital;
    }

    /**
     * Prints the report header for Country Reports.
     */
    public static void printHeader() {
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-40s | %-15s | %-20s | %-15s | %-15s |\n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Prints the details of a single Country record.
     */
    public void print() {
        // Use a NumberFormat to add commas to the population number
        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        System.out.printf("| %-4s | %-40s | %-15s | %-20s | %-15s | %-15s |\n",
                this.Code,
                this.Name,
                this.Continent,
                this.Region,
                nf.format(this.Population),
                this.Capital);
    }

    /**
     * Prints a formatted list of Country objects.
     * @param countries The list of Country objects to print.
     * @param title The title for the report.
     */
    public static void printReport(List<Country> countries, String title) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("---------------------------------------------------");
            System.out.println("No countries found for the report: " + title);
            System.out.println("---------------------------------------------------");
            return;
        }

        System.out.println("\n===================================================================================================================");
        System.out.println(" " + title);
        System.out.println("===================================================================================================================");

        printHeader();
        for (Country country : countries) {
            country.print();
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
    }
}