package com.rental.system.service;

import com.rental.system.model.Customer;
import com.rental.system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@SuppressWarnings("null")
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // --- The Memory ---
    public HashSet<Customer> getAllCustomers() {
        return new HashSet<>(customerRepository.findAll());
    }

    public void setCustomers(HashSet<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    public int getCount() {
        return (int) customerRepository.count();
    }

    // --- The Brain (Pure Logic) ---
    
    public void registerNewCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void removeCustomer(int id) {
        customerRepository.deleteById(id);
    }

    public void updateCustomerInDB(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer findById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void generateDefaultCustomers() {
        if (customerRepository.count() > 0) return;
        String[][] custs = {
                { "Aruna Smith", "D7654321", "0662345679", "IDCard.jpg", "DriverLicense.jpg" },
                { "Bona Johnson", "D2345678", "0122345680", "IDCard.jpg", "DriverLicense.jpg" },
                { "Champa Brown", "D3456789", "0172345681", "IDCard.jpg", "DriverLicense.jpg" },
                { "Diana Prince", "D4567890", "0882345682", "IDCard.jpg", "DriverLicense.jpg" },
                { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "" }
        };

        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2], cust[3], cust[4]);
            customerRepository.save(newCustomer);
        }
    }
}
