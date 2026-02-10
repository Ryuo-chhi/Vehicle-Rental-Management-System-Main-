import java.util.Objects;

public class Customer {
    private int customerId;
    private String customerName;
    private String customerIdCard;
    private String customerPhone;
    private String IDCardPhoto; // String path to photo of ID card
    private String DriverLicensePhoto; // String path to photo of driver license

    static int countCustomerId = 1;

    // register customer
    public Customer(String customerName, String customerIdCard, String customerPhone, String IDCardPhoto,
            String DriverLicensePhoto) {
        this.customerId = countCustomerId++;
        setCustomerIdCard(customerIdCard);
        setCustomerName(customerName);
        setCustomerPhone(customerPhone);
        this.IDCardPhoto = IDCardPhoto;
        this.DriverLicensePhoto = DriverLicensePhoto;
    }

    // for using and testing
    public Customer(String customerName, String customerIdCard, String customerPhone) {
        this.customerId = countCustomerId++;
        setCustomerIdCard(customerIdCard);
        setCustomerName(customerName);
        setCustomerPhone(customerPhone);
    }

    /*===== Getter =====*/
    public int getCustomerId() {
        return customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerIdCard() {
        return customerIdCard;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public String getIDCardPhoto() {
        return IDCardPhoto;
    }
    public String getDriverLicensePhoto() {
        return DriverLicensePhoto;
    }

    /*===== Setter =====*/
    public void setCustomerName(String customerName) {
        if(customerName == null || customerName.trim().isEmpty()) {
            System.out.println("Customer name cannot be null or empty.");
            return;
        }
        this.customerName = customerName;
    }
    public void setCustomerIdCard(String customerIdCard) {
        if(customerIdCard == null || customerIdCard.trim().isEmpty()) {
            System.out.println("Customer ID card cannot be null or empty.");
            return;
        }
        this.customerIdCard = customerIdCard;
    }
    public void setCustomerPhone(String customerPhone) {
        if(customerPhone == null || !customerPhone.matches("^[0-9]{9,10}$")) {
            System.out.println("Customer phone must be a 9-10-digit number.");
            return;
        }
        this.customerPhone = customerPhone;
    }
    public void setIDCardPhoto(String IDCardPhoto) {
        this.IDCardPhoto = IDCardPhoto;
    }
    public void setDriverLicensePhoto(String driverLicensePhoto) {
        DriverLicensePhoto = driverLicensePhoto;
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", idCard=\"" + customerIdCard
                + "\", phone=\"" + customerPhone + "\", IDCardPhoto=\"" + IDCardPhoto + "\", DriverLicensePhoto=\""
                + DriverLicensePhoto + "\"]";
    }

    // Simple version - no photo paths
    public String toStringSimple() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", idCard=\"" + customerIdCard
                + "\", phone=\"" + customerPhone + "\"]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Customer customer = (Customer) obj;
       return Objects.equals(customerId, customer.customerId) && Objects.equals(customerIdCard, customer.customerIdCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerIdCard);
    }
}

// Note: All Customer must be registered before renting a vehicle. All customer
// must be adult (18 years old or older).
