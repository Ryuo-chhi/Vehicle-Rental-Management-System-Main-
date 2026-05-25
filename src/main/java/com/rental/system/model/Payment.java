package com.rental.system.model;

public class Payment {
    private int paymentId;
    private String paymentMethod; // e.g., credit card, cash, online transfer, etc.
    private int rentDays;
    private double price;
    private double discount;
    private int extraDays;
    private double damageFee;
    private String payDate;
    private String status; // PENDING or PAID
    private double deposit;

    static int countPaymentId = 1; // to ensure unique payment IDs

    public Payment(int rentDays, double price, double deposit) {
        this.paymentId = countPaymentId++;
        this.setPaymentMethod("TBD");
        this.setPrice(price);
        this.setDiscount(0);
        this.setExtraDays(0);
        this.setDamageFee(0);
        this.setDeposit(deposit);
        this.setRentDays(rentDays);
        this.setPayDate("TBD");
        this.setStatus("PENDING");
    }

    public double calculateTotal() {
        double penaltyMultiplier = 1.5;
        double taxRate = 0.0;
        try {
            penaltyMultiplier = Double.parseDouble(com.rental.system.database.DatabaseMapper.getSetting("LATE_PENALTY_MULTIPLIER", "1.5"));
            taxRate = Double.parseDouble(com.rental.system.database.DatabaseMapper.getSetting("TAX_RATE", "0.0"));
        } catch (NumberFormatException e) {
            // fallback
        }

        // Base cost for the rental period
        double baseCost = price * this.rentDays;

        // Extra days cost with penalty multiplier
        double extraCost = price * extraDays * penaltyMultiplier;

        // Subtotal before discount
        double subtotal = baseCost + extraCost;

        // Apply discount (percentage)
        double discountAmount = subtotal * (discount / 100.0);

        // Before tax (subtotal - discount + damage fee)
        double beforeTax = subtotal - discountAmount + damageFee;

        // Apply tax (percentage)
        double taxAmount = beforeTax * (taxRate / 100.0);

        // Final total = beforeTax + taxAmount - deposit
        double total = beforeTax + taxAmount - deposit;
        
        // Ensure total isn't negative (deposit might exceed charges)
        if (total < 0.0) {
            total = 0.0;
        }

        return total;
    }

    public double expectedTotal() {
        double taxRate = 0.0;
        try {
            taxRate = Double.parseDouble(com.rental.system.database.DatabaseMapper.getSetting("TAX_RATE", "0.0"));
        } catch (NumberFormatException e) {
            // fallback
        }
        double base = price * this.rentDays;
        double subtotal = base - (base * (discount / 100.0));
        double withTax = subtotal * (1.0 + taxRate / 100.0);
        double total = withTax - deposit;
        return total < 0.0 ? 0.0 : total;
    }


    // ===== SETTERS (only for changeable fields) =====
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setRentDays(int rentDays) { //use this method to update rent days in case of extension or early return
        this.rentDays = rentDays > 0 ? rentDays : 1;
    }
    public int getRentDays() { return rentDays; }

    public String getPaymentMethod() { return paymentMethod; }
    public int getPaymentId()         { return paymentId; }
    public double getPrice()          { return price; }
    public double getDiscount()       { return discount; }
    public int getExtraDays()         { return extraDays; }
    public double getDamageFee()      { return damageFee; }
    public String getPayDate()        { return payDate; }
    public String getStatus()         { return status; }
    public double getDeposit()        { return deposit; }

    public void setPrice(double price) { // use this method to update price in case of vehicle change  (but not rate change)
        this.price = price > 0 ? price : 0.0;
    }

    public void setDiscount(double discount) {
        this.discount = (discount >= 0 && discount <= 100) ? discount : 0.0;
    }

    public void setExtraDays(int extraDays) {
        this.extraDays = extraDays > 0 ? extraDays : 0;
    }

    public void setDamageFee(double damageFee) {
        this.damageFee = damageFee > 0 ? damageFee : 0.0;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit > 0 ? deposit : 0.0;
    }

    public void setPaymentMethod(String method) {
        if (method != null &&
                (method.equalsIgnoreCase("CASH") ||
                        method.equalsIgnoreCase("CARD") ||
                        method.equalsIgnoreCase("ABA") ||
                        method.equalsIgnoreCase("ACLEDA") ||
                        method.equalsIgnoreCase("WING") ||
                        method.equalsIgnoreCase("TBD"))) {
            this.paymentMethod = method.toUpperCase();
        } else {
            this.paymentMethod = "TBD";
        }
    }

    public void setPayDate(String payDate) {
        this.payDate = (payDate != null && !payDate.trim().isEmpty()) ? payDate : "TBD";
    }

    public void setStatus(String status) {
        if (status != null && (status.equalsIgnoreCase("PENDING") || status.equalsIgnoreCase("PAID"))) {
            this.status = status.toUpperCase();
        } else {
            this.status = "PENDING";
        }
    }

    public void processPayment(String method, String payDate) {
        this.setPaymentMethod(method);
        this.setPayDate(payDate);
        this.setStatus("PAID");
    }


    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", Recorded price=" + price + "$" +
                ", discount=" + discount + "%" +
                ", extraDays=" + extraDays +
                ", damageFee=" + damageFee + "$" +
                ", payDate='" + payDate + '\'' +
                ", deposit=" + deposit + "$" +
                ", Expected total=" + expectedTotal() + "$" +
                ", Final total=" + calculateTotal() + "$" +
                '}';
    }
       
}
