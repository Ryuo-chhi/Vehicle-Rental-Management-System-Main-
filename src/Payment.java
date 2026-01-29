public class Payment {
    int paymentId;
    double amount;
    String paymentMethod; // e.g., credit card, cash, online transfer, etc.

    static int nextPaymentId = 1; // to ensure unique payment IDs

    public Payment(double amount, String paymentMethod) {
        this.paymentId = nextPaymentId++;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment [id=" + paymentId + ", amount=" + amount + ", method=" + paymentMethod + "]";
    }
}
