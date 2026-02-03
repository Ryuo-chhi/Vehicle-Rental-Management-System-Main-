class Customer {
    int customerId;
    String customerName;
    String customerIdCard;
    String customerPhone;

    static int countCustomerId = 1;

    Customer(String customerName, String customerIdCard, String customerPhone) {
        this.customerId = countCustomerId++;
        this.customerIdCard = customerIdCard;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", idCard=\"" + customerIdCard + "\", phone=\"" + customerPhone + "\"]";
    }
}
