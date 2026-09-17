package dev.prathamesh.ai.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for formatting values in responses
 * Helps maintain consistent formatting across the application
 */
public class FormattingUtils {

    private static final Locale INDIA_LOCALE = new Locale("en", "IN");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Format price in Indian Rupees
     * Example: 3000 -> 3,000
     */
    public static String formatPrice(Number amount) {
        if (amount == null) {
            return "0";
        }
        NumberFormat formatter = NumberFormat.getInstance(INDIA_LOCALE);
        return formatter.format(amount);
    }

    /**
     * Format price with currency symbol
     * Example: 3000 -> ₹3,000
     */
    public static String formatPriceWithCurrency(Number amount) {
        if (amount == null) {
            return "₹0";
        }
        return "₹" + formatPrice(amount);
    }

    /**
     * Format date to readable format
     * Example: 2024-09-24 -> September 24, 2024
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Format date to ISO format
     * Example: 2024-09-24
     */
    public static String formatDateISO(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(ISO_DATE_FORMATTER);
    }

    /**
     * Parse date string to LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, ISO_DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Pluralize noun based on count
     * Example: bed, 2 -> beds
     */
    public static String pluralize(String word, int count) {
        return count == 1 ? word : word + "s";
    }

    /**
     * Format number of guests
     * Example: 1 -> "1 guest", 2 -> "2 guests"
     */
    public static String formatGuests(int count) {
        return count + " " + pluralize("guest", count);
    }

    /**
     * Format number of beds
     * Example: 1 -> "1 bed", 2 -> "2 beds"
     */
    public static String formatBeds(int count) {
        return count + " " + pluralize("bed", count);
    }
}