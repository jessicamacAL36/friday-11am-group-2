package com.napier.sem;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents a city record from the world database.
 * This model is used for both City Reports and Capital City Reports.
 */
public class City {
    /** City Name */
    public String Name;
    /** Country Name */
    public String CountryName;
    /** District Name */
    public String District;
    /** City Population */
    public long Population;
    /** Flag to indicate if the report is a Capital City report (affects output format) */
    public boolean isCapital;

    /**
     * FIX: Added public no-argument constructor for unit testing purposes.
     */
    public City() {
    }

    /**
     * Constructor for City object (used by App.java mapper).
     */
    public City(String name, String countryName, String district, long population, boolean isCapital) {
        this.Name = name;
        this.CountryName = countryName;
        this.District = district;
        this.Population = population;
        this.isCapital = isCapital;
    }

    /**
     * Prints the report header for City Reports.
     */
    public static void printCityHeader() {
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.printf("| %-30s | %-30s | %-20s | %-15s |\n",
                "Name", "Country", "District", "Population");
        System.out.println("------------------------------------------------------------------------------------------------");
    }

    /**
     * Prints the report header for Capital City Reports.
     */
    public static void printCapitalHeader() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("| %-30s | %-30s | %-15s |\n",
                "Name", "Country", "Population");
        System.out.println("-----------------------------------------------------------------------");
    }

    /**
     * Prints the details of a single City record.
     */
    public void print() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        if (this.isCapital) {
            // Capital City Format (Name, Country, Population)
            System.out.printf("| %-30s | %-30s | %-15s |\n",
                    this.Name,
                    this.CountryName,
                    nf.format(this.Population));
        } else {
            // Standard City Format (Name, Country, District, Population)
            System.out.printf("| %-30s | %-30s | %-20s | %-15s |\n",
                    this.Name,
                    this.CountryName,
                    this.District != null ? this.District : "N/A",
                    nf.format(this.Population));
        }
    }

    /**
     * Prints a formatted list of City objects.
     * @param cities The list of City objects to print.
     * @param title The title for the report.
     */
    public static void printReport(List<City> cities, String title, boolean isCapitalReport) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("---------------------------------------------------");
            System.out.println("No data found for the report: " + title);
            System.out.println("---------------------------------------------------");
            return;
        }

        System.out.println("\n=====================================================================================================");
        System.out.println(" " + title);
        System.out.println("=====================================================================================================");

        if (isCapitalReport) {
            printCapitalHeader();
        } else {
            printCityHeader();
        }

        for (City city : cities) {
            city.print();
        }

        if (isCapitalReport) {
            System.out.println("-----------------------------------------------------------------------");
        } else {
            System.out.println("------------------------------------------------------------------------------------------------");
        }
    }
}