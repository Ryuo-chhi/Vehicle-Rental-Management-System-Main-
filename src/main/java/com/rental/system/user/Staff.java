package com.rental.system.user;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "staffs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "staff_role", discriminatorType = DiscriminatorType.STRING)
public abstract class Staff implements IStaff {
    /* ====== Fields (Encapsulation) ====== */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "full_name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    private boolean status;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "salary")
    private double salary;

    private static int staffCount = 0;

    public Staff() {}

    /*====== com.rental.system.user.Staff Permissions (must be defined by each role) ====== */
    @Override
    public abstract boolean can(String action);

    /*====== Register ====== */
    public Staff(String name, String username, String password, double salary) {
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