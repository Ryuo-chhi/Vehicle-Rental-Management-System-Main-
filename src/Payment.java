public class Payment {
    int paymentId;
    String paymentMethod; // e.g., credit card, cash, online transfer, etc.
    int rentDays;
    double price;
    double discount;
    int extraDays;
    double damageFee;
    String payDate;
    String status; // PENDING or PAID
    double deposit;

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

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", Recorded price=" + price + "$" +
                ", discount=" + discount + "%" +
                ", extraDays=" + extraDays +
                ", damageFee=" + damageFee + "$" +
                ", payDate='" + payDate + '\'' +
                ", deposit=" + deposit + "$" +
                ", Expected total=" + expectedTotal() + "$" +
                ", Final total=" + calculateTotal() + "$" +
                ", status='" + status + '\'' +
                '}';
    }
}
