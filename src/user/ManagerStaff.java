package user;

public class ManagerStaff extends Staff {

    private double salary;
    private double bonus;

    /*====== Register ====== */
    public ManagerStaff(String name, String username, String password, double salary) {
        super(name, username, password); // parent (user.Staff) runs first
        this.setSalary(salary);
        this.bonus = 0;
    }
   //Promote Regular to Manager
    public ManagerStaff(Staff staff, double salary) {
        super(staff.getName(), staff.getUsername(), staff.getPassword());
        this.setSalary(salary);
        this.setBonus(0);
    }

    /*====== Manager Permissions — full access ====== */
    @Override
    public boolean can(String action) {
        return true;
    }

    public double getSalary() {
        return salary;
    }
    public double getBonus() {return bonus;}

    public void setBonus(double bonus) {
        this.bonus = bonus>0 ? bonus : 0;
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
                ", bonus=" + bonus +
                "$}";
    }
}