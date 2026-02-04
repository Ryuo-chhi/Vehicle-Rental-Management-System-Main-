public class Customer {
    int customerId;
    String customerName;
    String customerIdCard;
    String customerPhone;
    String IDCardPhoto; // String path to photo of ID card
    String DriverLicensePhoto; // String path to photo of driver license

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
