package user;

import java.util.Objects;

public class Staff implements IStaff {
    /* ====== Fields (Encapsulation) ====== */
    private int staffId;
    private String name;
    private String role;
    private double salary;
    private String username;
    private String password;
    private boolean status;
    private boolean active;


    private static int staffCount = 0;

    /*====== user.Staff Permissions (default: no access — override in subclasses) ====== */
    @Override
    public boolean can(String action) {
        return false;
    }

    /*====== Register ====== */
    public Staff(String name, String role, double salary, String username, String password) {
        this.staffId = ++staffCount;
        this.setName(name);
        this.setRole(role);
        this.setSalary(salary);
        this.setUsername(username);
        this.setPassword(password);

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
    public String getRole() {
        return role;
    }
    @Override
    public double getSalary() {
        return salary;
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


    /*====== For login check ======*/
    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    /*====== Setters (with simple validation) ====== */
    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("user.Staff name cannot be null or empty.");
            return;
        }
        this.name = name.trim();
    }

    @Override
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            System.out.println("user.Staff role cannot be null or empty.");
            return;
        }
        this.role = role.trim();
    }

    @Override
    public void setSalary(double salary) {
        if (salary < 0) {
            System.out.println("Salary cannot be negative.");
            return;
        }
        this.salary = salary;
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

    // Additional Methods
    public static int getStaffCount() {
        return staffCount;
    }

    @Override
    public String toString() {
        return "user.Staff [staffId=" + staffId + ", name=" + name + ", role=" + role + ", username=" + username + ", salary=" + salary + ", status=" + status + ", active=" + active + "]";
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