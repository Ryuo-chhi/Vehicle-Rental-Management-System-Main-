class Payment {
    int paymentId;
    double amount;
    String paymentMethod;
    int extraDays;
    double damageFee;
    double snapshotRate;

    static int countPaymentId = 1;

    Payment(Rent rent, String paymentMethod) {
        this.paymentId = countPaymentId++;
        this.amount = rent.calculateTotal();
        this.paymentMethod = paymentMethod;
        this.extraDays = rent.extraDays;
        this.damageFee = rent.damageFee;
        this.snapshotRate = rent.snapshotRate;
    }

    double finalTotalPayment() {
        return amount + (extraDays * snapshotRate) + damageFee;
    }

    @Override
    public String toString() {
        return "Payment [id=" + paymentId + ", amount=" + amount + "$, method=" + paymentMethod + ", extraDays="
                + extraDays + ", damageFee=" + damageFee + "]";
    }
}
