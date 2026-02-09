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
        this.discount = 0.0;
        this.extraDays = 0;
        this.damageFee = 0.0;
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

    public void setDiscount(double discount) {
        if (discount >= 0) {
            this.discount = discount;
        }
    }

    public void setExtraDays(int extraDays) {
        if (extraDays >= 0) {
            this.extraDays = extraDays;
        }
    }

    public void setDamageFee(double damageFee) {
        if (damageFee >= 0) {
            this.damageFee = damageFee;
        }
    }

    public void processPayment(String method, String date) {
        if (method == null || method.isEmpty()) method = "TBD";
        if (date == null || date.isEmpty()) date = "TBD";
        this.paymentMethod = method;
        this.payDate = date;
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
