package com.rental.system.config;

public class SystemSettingsHolder {
    private static double taxRate = 0.0;
    private static double penaltyMultiplier = 1.5;
    private static int maxRentalDuration = 30;

    public static double getTaxRate() {
        return taxRate;
    }

    public static void setTaxRate(double rate) {
        taxRate = rate;
    }

    public static double getPenaltyMultiplier() {
        return penaltyMultiplier;
    }

    public static void setPenaltyMultiplier(double multiplier) {
        penaltyMultiplier = multiplier;
    }

    public static int getMaxRentalDuration() {
        return maxRentalDuration;
    }

    public static void setMaxRentalDuration(int duration) {
        maxRentalDuration = duration;
    }
}
