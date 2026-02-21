import java.util.Objects;

public class Staff implements IStaff {
    private int staffId;
    private String name;
    private String role;
    private double salary;
    private String username;
    private String password;
    private boolean status;

    private static int staffCount = 0;

    // register
    public Staff(String name, String role, double salary, String username, String password) {
        this.staffId = ++staffCount;
        this.setName(name);
        this.setRole(role);
        this.setSalary(salary);
        this.setUsername(username);
        this.setPassword(password);
        this.status = true;
    }

    // login
    public Staff(String username, String password) {
        this.setPassword(password);
        this.setUsername(username);
    }

    // IStaff Interface Methods - Getters
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

    // IStaff Interface Methods - Setters
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    // IStaff Permissions
    @Override
    public boolean can(String action) {
        if (action == null || role == null) return false;

        return switch (role.toUpperCase()) {
            case "ADMIN" -> true; // Admin can do everything
            case "MANAGER" -> canManager(action);
            case "STAFF" -> canStaff(action);
            default -> false;
        };
    }

    private boolean canStaff(String action) {
        return switch (action) {
            case "VIEW_VEHICLE", "VIEW_CUSTOMER", "ADD_RENT", "RETURN_VEHICLE", "SHOW_PAYMENT" -> true;
            default -> false;
        };
    }

    private boolean canManager(String action) {
        return switch (action) {
            case "VIEW_VEHICLE", "VIEW_CUSTOMER", "ADD_RENT", "RETURN_VEHICLE", "SHOW_PAYMENT",
                 "MANAGE_VEHICLE", "MANAGE_CUSTOMER", "VIEW_REPORTS", "MANAGE_STAFF" -> true;
            default -> false;
        };
    }

    // Additional Methods
    public static int getStaffCount() {
        return staffCount;
    }

    @Override
    public String toString() {
        return "Staff [staffId=" + staffId + ", name=" + name + ", role=" + role + ", salary=" + salary + ", status=" + status + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return staffId == staff.staffId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId);
    }
}