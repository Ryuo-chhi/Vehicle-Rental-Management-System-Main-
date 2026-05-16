package com.rental.system.user;

import java.util.Objects;

public abstract class Staff implements IStaff {
    /* ====== Fields (Encapsulation) ====== */
    private int staffId;
    private String name;
    private String username;
    private String password;
    private boolean status;
    private boolean active;
    private double salary;


    private static int staffCount = 0;

    /*====== com.rental.system.user.Staff Permissions (must be defined by each role) ====== */
    @Override
    public abstract boolean can(String action);

    /*====== Register ====== */
    public Staff(String name, String username, String password, double salary) {
        this.staffId = ++staffCount;
        this.setName(name);
        this.setUsername(username);
        this.setPassword(password);
        this.setSalary(salary);
        this.status = true;
        this.active = false; // Offline by default, becomes true when login
    }

    // login
    public Staff(String username, String password) {
        this.setPassword(password);
        this.setUsername(username);
    }

    /*====== Getters ====== */
    @Override
    public int getId() {
        return staffId;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean getStatus() {
        return status;
    }
    @Override
    public boolean isActive() { return active; }
    protected String getPassword(){
        return password;
    }
    public double getSalary() {
        return salary;
    }

    /*====== For login check ======*/
    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    /*====== Setters (with simple validation) ====== */
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    
    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("com.rental.system.user.Staff name cannot be null or empty.");
            return;
        }
        this.name = name.trim();
    }



    @Override
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be null or empty.");
            return;
        }
        this.username = username.trim();
    }

    @Override
    public void setPassword(String password) {
        String pw = (password == null) ? "" : password;
        if (pw.length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }
        this.password = password;
    }
    @Override
    public void setStatus(boolean status) {this.status = status;}
    @Override
    public void setActive(boolean active) {this.active = active;}
    public void setSalary (double salary){
        this.salary = salary >0 ? salary: 0;
    }
    // Additional Methods
    public static int getStaffCount() {
        return staffCount;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", active=" + active +
                ", salary=" + salary +
                "$";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return staffId == staff.staffId && Objects.equals(username, staff.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, username);
    }
}