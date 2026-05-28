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
    private Staff loggedInStaff;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
        this.loggedInStaff = null;
    }

    // --- The Memory ---
    public HashSet<Staff> getAllStaff() {
        return new HashSet<>(staffRepository.findAll());
    }

    public void setStaffs(HashSet<Staff> staffs) {
        staffRepository.saveAll(staffs);
        ensureAdminExists();
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
        staffRepository.save(staff);
    }

    public void updateStaffInDB(Staff staff) {
        staffRepository.save(staff);
    }

    public void removeStaff(Staff staff) {
        staffRepository.delete(staff);
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

    public void ensureAdminExists() {
        String adminUser = "admin_root";
        Optional<Staff> opt = staffRepository.findByUsername(adminUser);
        if (opt.isEmpty()) {
            addAdmin("Admin", adminUser, "root123");
        }
    }

    public void addAdmin(String name, String username, String password) {
        Staff admin = new ManagerStaff(name, username, password, 0);
        staffRepository.save(admin);
    }

    public void generateDefaultStaff() {
        ensureAdminExists();
        if (staffRepository.findByUsername("bob_manager").isEmpty()) {
            Staff s2 = new ManagerStaff("Bob", "bob_manager", "manager123", 0);
            staffRepository.save(s2);
        }
        if (staffRepository.findByUsername("chan_staff").isEmpty()) {
            Staff s3 = new RegularStaff("Chan", "chan_staff", "staff123", 1500, "Station-Moto");
            staffRepository.save(s3);
        }
    }
}
