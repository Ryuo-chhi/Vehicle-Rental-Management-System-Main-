public class Staff {
    private int staffId;
    private String name;
    private String role;
    private double salary;

    private static int staffCount = 0;

    public Staff(String name, String role, double salary) {
        this.staffId = ++staffCount;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public static int getStaffCount() {
        return staffCount;
    }

    @Override
    public String toString() {
        return "Staff [staffId=" + staffId + ", name=" + name + ", role=" + role + ", salary=" + salary + "]";
    }

}
