package com.rental.system.service;

import com.rental.system.model.Customer;
import com.rental.system.database.DatabaseMapper;
import java.util.HashSet;

public class CustomerService {
    private HashSet<Customer> customers;

    public CustomerService() {
        this.customers = new HashSet<>();
    }

    // --- The Memory ---
    public HashSet<Customer> getAllCustomers() {
        return customers;
    }

    public void setCustomers(HashSet<Customer> customers) {
        this.customers = customers;
    }

    public int getCount() {
        return customers.size();
    }

    // --- The Brain (Pure Logic) ---
    
    public void registerNewCustomer(Customer customer) {
        customers.add(customer);
        DatabaseMapper.saveNewCustomer(customer);
    }

    public void removeCustomer(int id) {
        customers.removeIf(c -> c.getCustomerId() == id);
        DatabaseMapper.deleteCustomer(id);
    }

    public void updateCustomerInDB(Customer customer) {
        DatabaseMapper.updateCustomer(customer);
    }

    public Customer findById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) return c;
        }
        return null;
    }

    public void generateDefaultCustomers() {
        String[][] custs = {
                { "Aruna Smith", "D7654321", "0662345679", "IDCard.jpg", "DriverLicense.jpg" },
                { "Bona Johnson", "D2345678", "0122345680", "IDCard.jpg", "DriverLicense.jpg" },
                { "Champa Brown", "D3456789", "0172345681", "IDCard.jpg", "DriverLicense.jpg" },
                { "Diana Prince", "D4567890", "0882345682", "IDCard.jpg", "DriverLicense.jpg" },
                { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "" }
        };

        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2], cust[3], cust[4]);
            customers.add(newCustomer);
        }
    }
}
