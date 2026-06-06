package com.rental.system.service;

import com.rental.system.user.ManagerStaff;
import com.rental.system.user.RegularStaff;
import com.rental.system.user.Staff;
import com.rental.system.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class StaffService {
    private final StaffRepository staffRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private Staff loggedInStaff;

    @Autowired
    public StaffService(StaffRepository staffRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.loggedInStaff = null;
    }

    // --- The Memory ---
    public HashSet<Staff> getAllStaff() {
        return new HashSet<>(staffRepository.findAll());
    }

    public void setStaffs(HashSet<Staff> staffs) {
        staffRepository.saveAll(staffs);
    }

    public int getStaffCount() {
        return (int) staffRepository.count();
    }

    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }

    public boolean isStaffLoggedIn() {
        return loggedInStaff != null;
    }

    // --- The Brain (Pure Logic) ---

    public void registerNewStaff(Staff staff) {
        if (staff.getPassword() != null) {
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        staffRepository.save(staff);
    }

    public void updateStaffInDB(Staff staff) {
        if (staff.getPassword() != null && !staff.getPassword().startsWith("$2a$")) {
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        staffRepository.save(staff);
    }

    public void removeStaff(Staff staff) {
        staffRepository.delete(staff);
    }

    public Optional<Staff> getStaffById(int id) {
        return staffRepository.findById(id);
    }

    public Staff findByIdAndUsername(int id, String username) {
        Optional<Staff> opt = staffRepository.findById(id);
        if (opt.isPresent() && opt.get().getUsername().equalsIgnoreCase(username.trim())) {
            return opt.get();
        }
        return null;
    }

    public String login(String username, String password) {
        if (username.isBlank() || password == null) {
            return "Login failed: missing username/password.";
        }

        Optional<Staff> opt = staffRepository.findByUsername(username.trim());
        if (opt.isPresent()) {
            Staff s = opt.get();
            if (!s.getStatus()) {
                return "Login failed: staff is no longer employed.";
            }
            if (!s.checkPassword(password)) {
                return "Login failed: wrong password.";
            }

            s.setActive(true);
            staffRepository.save(s);
            loggedInStaff = s;
            return "Login success. Welcome " + s.getUsername() + "!";
        }
        return "Login failed: username not found.";
    }

    public void logout() {
        if (loggedInStaff != null) {
            loggedInStaff.setActive(false);
            staffRepository.save(loggedInStaff);
        }
        loggedInStaff = null;
    }

    public void addAdmin(String name, String username, String password) {
        Staff admin = new ManagerStaff(name, username, passwordEncoder.encode(password), 0);
        staffRepository.save(admin);
    }

    public void generateDefaultStaff() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.io.InputStream is = getClass().getResourceAsStream("/staff-seeds.json");
            if (is == null) {
                System.out.println("Seed file staff-seeds.json not found in resources.");
                return;
            }
            java.util.List<java.util.Map<String, Object>> seeds = mapper.readValue(is, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            for (java.util.Map<String, Object> seed : seeds) {
                String username = (String) seed.get("username");
                if (staffRepository.findByUsername(username).isEmpty()) {
                    String role = (String) seed.get("role");
                    String name = (String) seed.get("name");
                    String password = (String) seed.get("password");
                    double salary = ((Number) seed.getOrDefault("salary", 0.0)).doubleValue();

                    Staff s;
                    if ("MANAGER".equalsIgnoreCase(role)) {
                        s = new ManagerStaff(name, username, passwordEncoder.encode(password), salary);
                    } else {
                        String workStation = (String) seed.getOrDefault("workStation", "");
                        s = new RegularStaff(name, username, passwordEncoder.encode(password), salary, workStation);
                    }
                    staffRepository.save(s);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load staff seeds: " + e.getMessage());
        }
    }
}
