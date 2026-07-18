package com.rental.system.controller;

import com.rental.system.model.Customer;
import com.rental.system.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.rental.system.security.JwtTokenProvider;
import com.rental.system.security.CustomerPrincipal;

import jakarta.validation.Valid;

import java.util.Set;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public CustomerController(CustomerService customerService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping
    public Set<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {
        customerService.registerNewCustomer(customer);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerLoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            CustomerPrincipal principal = (CustomerPrincipal) authentication.getPrincipal();
            Customer customer = principal.getCustomer();

            String welcomeMessage = "Login success. Welcome " + customer.getCustomerName() + "!";
            return ResponseEntity.ok(new CustomerLoginResponse(jwt, welcomeMessage, customer));
        } catch (AuthenticationException ex) {
            return ResponseEntity.badRequest().body("Login failed: wrong password or email not found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @Valid @RequestBody Customer customerDetails) {
        Customer existing = customerService.findById(id);
        if (existing != null) {
            existing.setCustomerName(customerDetails.getCustomerName());
            existing.setcustomerIdNum(customerDetails.getcustomerIdNum());
            existing.setCustomerPhone(customerDetails.getCustomerPhone(), null);
            existing.setEmail(customerDetails.getEmail());
            // Intentionally omit password update to avoid overriding with plain text / empty fields here
            customerService.updateCustomerInDB(existing);
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            customerService.removeCustomer(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    @NoArgsConstructor
    public static class CustomerLoginRequest {
        private String email;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerLoginResponse {
        private String token;
        private String message;
        private Customer customer;
    }
}
