public class Payment {
    int paymentId;
    double amount;
    String paymentMethod; // e.g., credit card, cash, online transfer, etc.
    int extraDays;
    double damageFee;

    static int countPaymentId = 1; // to ensure unique payment IDs

    public Payment(Rent rent, String paymentMethod) {
        this.paymentId = countPaymentId++;
        this.amount = rent.calculateTotal();
        this.paymentMethod = paymentMethod;
        this.extraDays = rent.extraDays; // Get from Rent
        this.damageFee = rent.damageFee; // Get from Rent
    }
    

    public double finalTotalPayment() {
        return amount + extraDays * (amount / (amount / extraDays)) + damageFee;
    }

    @Override
    public String toString() {
        return "Payment [id=" + paymentId + ", amount=" + amount + "$ , method=" + paymentMethod + ", extraDays="
                + extraDays + ", damageFee=" + damageFee + "]";
    }

}
