package user;

public class ManagerStaff extends Staff {

    double salary;

    /*====== Register ====== */
    public ManagerStaff(String name, String role, double salary, String username, String password) {
        super(name, role, username, password); // parent (user.Staff) runs first
        this.setSalary(salary);
    }

    /*====== Manager Permissions — full access ====== */
    @Override
    public boolean can(String action) {
        return true;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ManagerStaff that = (ManagerStaff) o;
        return this.getUsername().equals(that.getUsername()) && this.getId() == that.getId();
    }
    @Override
    public String toString() {
        return "ManagerStaff{" +
                super.toString() +
                ", salary=" + salary +
                "$}";
    }
}