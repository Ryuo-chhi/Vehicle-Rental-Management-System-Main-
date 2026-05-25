package com.rental.system.model;

public class Promotion {
    private int promoId;
    private String code;
    private double discountPercent;
    private boolean isActive;

    public Promotion(String code, double discountPercent) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.isActive = true;
    }

    public Promotion(int promoId, String code, double discountPercent, boolean isActive) {
        this.promoId = promoId;
        this.code = code;
        this.discountPercent = discountPercent;
        this.isActive = isActive;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return String.format("Promo ID: %d | Code: %-10s | Discount: %5.1f%% | Status: %s",
                promoId, code, discountPercent, isActive ? "ACTIVE" : "INACTIVE");
    }
}
