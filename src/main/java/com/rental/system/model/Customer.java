package com.rental.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @NotBlank(message = "Customer name is required")
    @Column(name = "full_name", nullable = false)
    private String customerName;

    @NotBlank(message = "ID Card number is required")
    @Column(name = "id_num", nullable = false)
    private String customerIdNum;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "Phone must be 9-10 digits")
    @Column(name = "phone", nullable = false)
    private String customerPhone;

    @Column(name = "id_card_photo")
    private String IDCardPhoto; // String path to photo of ID card

    @Column(name = "dl_photo")
    private String DriverLicensePhoto; // String path to photo of driver license

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    @Column(name = "password_hash")
    private String password;

    private static int countCustomerId = 1;

    public Customer() {}

    // register customer
    public Customer(String customerName, String customerIdNum, String customerPhone, String IDCardPhoto,
            String DriverLicensePhoto) {
        this.setcustomerIdNum(customerIdNum);
        this.setCustomerName(customerName);
        this.setCustomerPhone(customerPhone, null );
        this.IDCardPhoto = IDCardPhoto;
        this.DriverLicensePhoto = DriverLicensePhoto;
    }

    // for using and testing
    public Customer(String customerName, String customerIdNum, String customerPhone) {
        this.setcustomerIdNum(customerIdNum);
        this.setCustomerName(customerName);
        this.setCustomerPhone(customerPhone, null );
    }

    /*===== Getter =====*/
    public int getCustomerId() {
        return customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getcustomerIdNum() {
        return customerIdNum;
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
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public static int getCountCustomerId() {
        return countCustomerId;
    }

    /*===== Setter =====*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setCustomerName(String customerName) {
        
        if(customerName == null || customerName.trim().isEmpty()) {
            System.out.println("Customer name cannot be null or empty.");
            return;
        }
        this.customerName = customerName;
    }
    public void setcustomerIdNum(String customerIdNum) {
        if(customerIdNum == null || customerIdNum.trim().isEmpty()) {
            System.out.println("Customer ID card cannot be null or empty.");
            return;
        }
        this.customerIdNum = customerIdNum;
    }
    public void setCustomerPhone(String customerPhone, HashSet<Customer> customers) {
        if(customerPhone == null || customerPhone.trim().isEmpty() || !customerPhone.matches("^[0-9]{9,10}$")) {
            System.out.println("Customer phone must be a 9-10-digit number.");
            return;
        }
        if(customers != null && isPhoneExisted(customerPhone, customers)){
            System.out.println("Customer phone number already exists.");
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
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPhoneExisted(String phone, HashSet<Customer> customers) {
        if (customers == null) {
            return false;
        }
        for (Customer customer : customers) {
            if (customer == null) continue;
            String existingPhone = customer.getCustomerPhone();
            if (existingPhone != null && existingPhone.equals(phone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", email=\"" + email + "\", idCard=\"" + customerIdNum
                + "\", phone=\"" + customerPhone + "\", IDCardPhoto=\"" + IDCardPhoto + "\", DriverLicensePhoto=\""
                + DriverLicensePhoto + "\"]";
    }

    // Simple version - no photo paths
    public String toStringSimple() {
        return "Customer [id=" + customerId + ", name=\"" + customerName + "\", email=\"" + email + "\", idCard=\"" + customerIdNum
                + "\", phone=\"" + customerPhone + "\"]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerIdNum == null) ? 0 : customerIdNum.hashCode());
        result = prime * result + ((customerPhone == null) ? 0 : customerPhone.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (customerIdNum == null) {
            if (other.customerIdNum != null)
                return false;
        } else if (!customerIdNum.equals(other.customerIdNum))
            return false;
        if (customerPhone == null) {
            if (other.customerPhone != null)
                return false;
        } else if (!customerPhone.equals(other.customerPhone))
            return false;
        return true;
    }

    
}

// Note: All Customer must be registered before renting a vehicle. All customer
// must be adult (18 years old or older).
