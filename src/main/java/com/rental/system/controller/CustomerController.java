package com.rental.system.controller;

import com.rental.system.model.Customer;
import com.rental.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        } catch (Exception ex) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            customerService.removeCustomer(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class CustomerLoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class CustomerLoginResponse {
        private String token;
        private String message;
        private Customer customer;

        public CustomerLoginResponse(String token, String message, Customer customer) {
            this.token = token;
            this.message = message;
            this.customer = customer;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Customer getCustomer() { return customer; }
        public void setCustomer(Customer customer) { this.customer = customer; }
    }
}
