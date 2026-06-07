package com.rental.system.controller;

import com.rental.system.user.ManagerStaff;
import com.rental.system.user.RegularStaff;
import com.rental.system.user.Staff;
import com.rental.system.model.Customer;
import com.rental.system.service.StaffService;
import com.rental.system.service.CustomerService;
import com.rental.system.security.JwtTokenProvider;
import com.rental.system.security.StaffPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
            return ResponseEntity.ok(new LoginResponse(jwt, welcomeMessage, staff.getUsername(), staff.getRole()));
        } catch (Exception ex) {
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

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String token;
        private String message;
        private String username;
        private String role;

        public LoginResponse(String token, String message, String username, String role) {
            this.token = token;
            this.message = message;
            this.username = username;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
