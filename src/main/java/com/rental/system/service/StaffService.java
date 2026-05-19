package com.rental.system.service;

import com.rental.system.model.Customer;
import com.rental.system.user.ManagerStaff;
import com.rental.system.user.RegularStaff;
import com.rental.system.user.Staff;
import com.rental.system.database.DatabaseMapper;
import java.util.HashSet;

public class StaffService {
    private HashSet<Staff> staffs;
    private Staff loggedInStaff;

    public StaffService() {
        this.staffs = new HashSet<>();
        this.loggedInStaff = null;
    }

    // --- The Memory ---
    public HashSet<Staff> getAllStaff() {
        return staffs;
    }

    public void setStaffs(HashSet<Staff> staffs) {
        this.staffs = staffs;
        ensureAdminExists();
    }

    public int getStaffCount() {
        return staffs.size();
    }

    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }

    public boolean isStaffLoggedIn() {
        return loggedInStaff != null;
    }

    // --- The Brain (Pure Logic) ---

    public void registerNewStaff(Staff staff) {
        staffs.add(staff);
        DatabaseMapper.saveNewStaff(staff);
    }

    public void updateStaffInDB(Staff staff) {
        DatabaseMapper.updateStaff(staff);
    }

    public void removeStaff(Staff staff) {
        if (staffs.remove(staff)) {
            DatabaseMapper.deleteStaff(staff.getId());
        }
    }

    public Staff findByIdAndUsername(int id, String username) {
        for (Staff s : staffs) {
            if (s.getId() == id && s.getUsername().equalsIgnoreCase(username.trim())) {
                return s;
            }
        }
        return null;
    }

    public String login(String username, String password) {
        if (username.isBlank() || password == null) {
            return "Login failed: missing username/password.";
        }

        for (Staff s : staffs) {
            if (s.getUsername().equalsIgnoreCase(username.trim())) {
                if (!s.getStatus()) {
                    return "Login failed: staff is no longer employed.";
                }
                if (!s.checkPassword(password)) {
                    return "Login failed: wrong password.";
                }

                s.setActive(true);
                loggedInStaff = s;
                return "Login success. Welcome " + s.getUsername() + "!";
            }
        }
        return "Login failed: username not found.";
    }

    public void logout() {
        if (loggedInStaff != null) {
            loggedInStaff.setActive(false);
        }
        loggedInStaff = null;
    }

    public void ensureAdminExists() {
        String adminUser = "admin_root";
        boolean exists = false;
        for (Staff s : staffs) {
            if (s.getUsername().equalsIgnoreCase(adminUser)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            addAdmin("Admin", adminUser, "root123");
        }
    }

    public void addAdmin(String name, String username, String password) {
        Staff admin = new ManagerStaff(name, username, password, 0) {
            @Override
            public boolean can(String action) {
                return true; // Super admin: can do everything
            }
        };
        staffs.add(admin);
    }

    public void generateDefaultStaff() {
        ensureAdminExists();
        Staff s2 = new ManagerStaff("Bob", "bob_manager", "manager123", 0);
        Staff s3 = new RegularStaff("Chan", "chan_staff", "staff123", 1500, "Station-Moto");
        staffs.add(s3);
        staffs.add(s2);
    }
}
