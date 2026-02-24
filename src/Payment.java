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
        this.paymentMethod = "TBD";
        this.price = price;
        this.setDiscount(0);
        this.setExtraDays(0);
        this.setDamageFee(0);
        this.deposit = deposit > 0 ? deposit : 0.0;
        this.rentDays = rentDays;
        this.payDate = "TBD";
        this.status = "PENDING";
    }

    public double calculateTotal() {
        // Base cost for the rental period
        double baseCost = price * this.rentDays;

        // Extra days cost (same daily rate)
        double extraCost = price * extraDays;

        // Subtotal before discount
        double subtotal = baseCost + extraCost;

        // Apply discount (percentage)
        double discountAmount = subtotal * (discount / 100.0);

        // Final total = subtotal - discount + damage fee - deposit
        double total = subtotal - discountAmount + damageFee - deposit;
        // Ensure total isn't negative (deposit might exceed charges)
        if (total < 0.0) {
            total = 0.0;
        }

        return total;
    }

    public double expectedTotal() {
        return price * this.rentDays - deposit;
    }


    // ===== SETTERS (only for changeable fields) =====

    public void setRentDays(int rentDays) { //use this method to update rent days in case of extension or early return
        this.rentDays = rentDays;
    }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPrice(double price) { // use this method to update price in case of vehicle change  (but not rate change)
        this.price = price;
    }

    public void setDiscount(double discount) {
            this.discount = discount>0 ? discount : 0.0;
    }

    public void setExtraDays(int extraDays) {
            this.extraDays = extraDays>0 ? extraDays : 0;
    }

    public void setDamageFee(double damageFee) {
            this.damageFee = damageFee>0 ? damageFee: 0.0;
    }

    public void processPayment(String method, String payDate) {

        // Validate payment method
        if (method != null &&
                (method.equalsIgnoreCase("CASH") ||
                        method.equalsIgnoreCase("CARD") ||
                        method.equalsIgnoreCase("ABA") ||
                        method.equalsIgnoreCase("ACLEDA") ||
                        method.equalsIgnoreCase("WING")))
                        {
            this.paymentMethod = method.toUpperCase();
        } else {
            this.paymentMethod = "TBD";
        }

        this.payDate = payDate;
        this.status = "PAID";
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
