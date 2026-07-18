package com.rental.system.controller;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.system.model.Customer;
import com.rental.system.security.JwtTokenProvider;
import com.rental.system.security.StaffPrincipal;
import com.rental.system.service.CustomerService;
import com.rental.system.service.StaffService;
import com.rental.system.user.ManagerStaff;
import com.rental.system.user.RegularStaff;
import com.rental.system.user.Staff;

@RestController
@RequestMapping("/api/staffs")
@SuppressWarnings("null")
public class StaffController {
    private final StaffService staffService;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public StaffController(StaffService staffService, CustomerService customerService, AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
        this.staffService = staffService;
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping
    public Set<Staff> getAllStaff() {
        return staffService.getAllStaff();
    }

    @PostMapping("/managers")
    public ResponseEntity<Staff> registerManager(@RequestBody ManagerStaff manager) {
        staffService.registerNewStaff(manager);
        return ResponseEntity.ok(manager);
    }

    @PostMapping("/register")
    public ResponseEntity<Staff> registerRegular(@RequestBody RegularStaff regular) {
        staffService.registerNewStaff(regular);
        return ResponseEntity.ok(regular);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        customerService.registerNewCustomer(customer);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            StaffPrincipal principal = (StaffPrincipal) authentication.getPrincipal();
            Staff staff = principal.getStaff();
            staff.setActive(true);
            staffService.updateStaffInDB(staff);

            String welcomeMessage = "Login success. Welcome " + staff.getUsername() + "!";
            return ResponseEntity.ok(new LoginResponse(jwt, welcomeMessage, staff.getUsername(), staff.getRole(), staff.getId()));
        } catch (AuthenticationException ex) {
            return ResponseEntity.badRequest().body("Login failed: wrong password or username not found.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof StaffPrincipal principal) {
            Staff staff = principal.getStaff();
            staff.setActive(false);
            staffService.updateStaffInDB(staff);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Staff> getCurrentStaff() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof StaffPrincipal principal) {
            return ResponseEntity.ok(principal.getStaff());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        java.util.Optional<Staff> staffOpt = staffService.getStaffById(id);
        if (staffOpt.isPresent()) {
            staffService.removeStaff(staffOpt.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable int id, @RequestBody StaffUpdateRequest request) {
        java.util.Optional<Staff> staffOpt = staffService.getStaffById(id);
        if (staffOpt.isPresent()) {
            Staff existing = staffOpt.get();
            existing.setName(request.getName());
            existing.setUsername(request.getUsername());
            existing.setSalary(request.getSalary());
            existing.setStatus(request.isStatus());
            
            if (existing instanceof RegularStaff regular) {
                if (request.getWorkStation() != null) {
                    regular.setWorkStation(request.getWorkStation());
                }
            }
            // Not allowing password updates here for simplicity, similar to customer update
            staffService.updateStaffInDB(existing);
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    @NoArgsConstructor
    public static class StaffUpdateRequest {
        private String name;
        private String username;
        private double salary;
        private boolean status;
        private String workStation;
    }

    @Data
    @NoArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String message;
        private String username;
        private String role;
        private int staffId;
    }
}
