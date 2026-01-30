public class Customer {
    int customerId;
    String customerName;
    String customerIdCard; // national id card number, passport number, driver's license number, etc.
    String customerPhone;

    static int nextCustomerId = 1; // this var keeps track of the next customer ID to assign. why static? because it's shared among all instances in order to ensure unique IDs.

    public Customer( String customerName, String customerIdCard, String customerPhone) {
        this.customerId = nextCustomerId++;
        this.customerIdCard = customerIdCard;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", idCard=\"" + customerIdCard + "\", phone=\"" + customerPhone + "\"]";
    }
}
