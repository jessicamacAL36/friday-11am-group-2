package com.napier.sem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Represents the population distribution for a Continent, Region, or Country
 * (UC23, UC24, UC25).
 */
public class PopulationSummary {
    /** Name of the entity (Continent, Region, or Country) */
    public String Name;
    /** Total population of the entity */
    public long TotalPopulation;
    /** Population living in cities */
    public long CityPopulation;
    /** Percentage of population living in cities */
    public double CityPopulationPercent;
    /** Population not living in cities (Rural/Non-City) */
    public long RuralPopulation;
    /** Percentage of population not living in cities (Rural/Non-City) */
    public double RuralPopulationPercent;

    /**
     * Constructor for PopulationSummary object.
     */
    public PopulationSummary(String name, long totalPop, long cityPop, double cityPopPct, long ruralPop, double ruralPopPct) {
        this.Name = name;
        this.TotalPopulation = totalPop;
        this.CityPopulation = cityPop;
        this.CityPopulationPercent = cityPopPct;
        this.RuralPopulation = ruralPop;
        this.RuralPopulationPercent = ruralPopPct;
    }

    /**
     * Prints the report header for Population Distribution Reports.
     */
    public static void printHeader() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-25s | %-18s | %-18s | %-10s | %-18s | %-10s |\n",
                "Entity", "Total Pop", "City Pop", "City %", "Non-City Pop", "Non-City %");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Prints the details of a single PopulationSummary record.
     */
    public void print() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        System.out.printf("| %-25s | %-18s | %-18s | %-10.2f | %-18s | %-10.2f |\n",
                this.Name,
                nf.format(this.TotalPopulation),
                nf.format(this.CityPopulation),
                this.CityPopulationPercent,
                nf.format(this.RuralPopulation),
                this.RuralPopulationPercent);
    }


    /**
     * Prints a formatted list of PopulationSummary objects.
     * @param summaries The list of PopulationSummary objects to print.
     * @param title The title for the report.
     */
    public static void printReport(List<PopulationSummary> summaries, String title) {
        if (summaries == null || summaries.isEmpty()) {
            System.out.println("---------------------------------------------------");
            System.out.println("No data found for the report: " + title);
            System.out.println("---------------------------------------------------");
            return;
        }


        System.out.println("\n====================================================================================================================================================");
        System.out.println(" " + title);
        System.out.println("====================================================================================================================================================");

        printHeader();
        for (PopulationSummary summary : summaries) {
            summary.print();
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}