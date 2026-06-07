package com.rental.system.service;

import com.rental.system.model.Customer;
import com.rental.system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
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
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
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

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void generateDefaultCustomers() {
        if (customerRepository.count() > 0) return;
        String[][] custs = {
                { "Aruna Smith", "D7654321", "0662345679", "IDCard.jpg", "DriverLicense.jpg", "aruna@test.com" },
                { "Bona Johnson", "D2345678", "0122345680", "IDCard.jpg", "DriverLicense.jpg", "bona@test.com" },
                { "Champa Brown", "D3456789", "0172345681", "IDCard.jpg", "DriverLicense.jpg", "champa@test.com" },
                { "Diana Prince", "D4567890", "0882345682", "IDCard.jpg", "DriverLicense.jpg", "diana@test.com" },
                { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "", "eno@test.com" }
        };

        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2], cust[3], cust[4]);
            newCustomer.setEmail(cust[5]);
            newCustomer.setPassword(passwordEncoder.encode("password123"));
            customerRepository.save(newCustomer);
        }
    }
}
