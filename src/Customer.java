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
        this.customerIdCard = customerIdCard;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.IDCardPhoto = IDCardPhoto;
        this.DriverLicensePhoto = DriverLicensePhoto;
    }

    // for using and testing
    public Customer(String customerName, String customerIdCard, String customerPhone) {
        this.customerId = countCustomerId++;
        this.customerIdCard = customerIdCard;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
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
        this.customerName = customerName;
    }
    public void setCustomerIdCard(String customerIdCard) {
        this.customerIdCard = customerIdCard;
    }
    public void setCustomerPhone(String customerPhone) {
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
}

// Note: All Customer must be registered before renting a vehicle. All customer
// must be adult (18 years old or older).
