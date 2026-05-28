package com.rental.system.controller;

import com.rental.system.user.ManagerStaff;
import com.rental.system.user.RegularStaff;
import com.rental.system.user.Staff;
import com.rental.system.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
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

    @PostMapping("/regulars")
    public ResponseEntity<Staff> registerRegular(@RequestBody RegularStaff regular) {
        staffService.registerNewStaff(regular);
        return ResponseEntity.ok(regular);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String message = staffService.login(request.getUsername(), request.getPassword());
        if (message.contains("success")) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().body(message);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        staffService.logout();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Staff> getCurrentStaff() {
        Staff current = staffService.getLoggedInStaff();
        if (current != null) {
            return ResponseEntity.ok(current);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        Staff staff = staffService.getAllStaff().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
        if (staff != null) {
            staffService.removeStaff(staff);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
