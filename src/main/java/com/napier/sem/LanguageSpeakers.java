package com.napier.sem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Represents the data for the Global Language Speakers Report (UC32).
 */
public class LanguageSpeakers {
    /** Name of the language */
    public String Language;
    /** Estimated number of speakers */
    public double Speakers;
    /** Percentage of world population */
    public double WorldPopulationPercent;

    /**
     * Constructor for LanguageSpeakers object.
     */
    public LanguageSpeakers(String language, double speakers, double worldPopulationPercent) {
        this.Language = language;
        this.Speakers = speakers;
        this.WorldPopulationPercent = worldPopulationPercent;
    }

    /**
     * Prints the report header for Language Speakers Report.
     */
    public static void printHeader() {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-25s | %-20s |\n",
                "Language", "Total Speakers (Est.)", "World Pop (%)");
        System.out.println("---------------------------------------------------------------------------------");
    }

    /**
     * Prints the details of a single LanguageSpeakers record.
     */
    public void print() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        // Ensure speakers are formatted as a large integer (long) for clarity
        String formattedSpeakers = nf.format((long) this.Speakers);

        System.out.printf("| %-15s | %-25s | %-20.4f |\n",
                this.Language,
                formattedSpeakers,
                this.WorldPopulationPercent);
    }



    /**
     * Prints a formatted list of LanguageSpeakers objects.
     * @param speakers The list of LanguageSpeakers objects to print.
     * @param title The title for the report.
     */
    public static void printReport(List<LanguageSpeakers> speakers, String title) {
        if (speakers == null || speakers.isEmpty()) {
            System.out.println("---------------------------------------------------");
            System.out.println("No data found for the report: " + title);
            System.out.println("---------------------------------------------------");
            return;
        }

        System.out.println("\n=================================================================================");
        System.out.println(" " + title);
        System.out.println("=================================================================================");

        printHeader();
        for (LanguageSpeakers speaker : speakers) {
            speaker.print();
        }
        System.out.println("---------------------------------------------------------------------------------");
    }
}