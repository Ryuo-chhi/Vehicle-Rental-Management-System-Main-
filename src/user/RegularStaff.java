package user;

import controller.Garage;

import java.util.Objects;

public class RegularStaff extends Staff {

    private double salary;

    /*====== Register ====== */
    public RegularStaff(String name,  String username, String password,double salary) {
        super(name, username, password); // parent (user.Staff) runs first
        this.setSalary(salary);
    }

    public RegularStaff(Staff staff, double salary) {
        super(staff.getName(), staff.getUsername(), staff.getPassword());
        this.setSalary(salary);
    }

    /*====== Regular user.Staff Permissions ====== */
    @Override
    public boolean can(String action) {
        return action.equals(Garage.VIEW_VEHICLE)    ||
               action.equals(Garage.VIEW_CUSTOMER)   ||
               action.equals(Garage.MANAGE_CUSTOMER) ||
               action.equals(Garage.ADD_RENT)        ||
               action.equals(Garage.VIEW_RENT)       ||
               action.equals(Garage.RETURN_VEHICLE)  ||
               action.equals(Garage.SHOW_PAYMENT);
    }


    public void setSalary (double salary){
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegularStaff that = (RegularStaff) o;
        return this.getUsername().equals(that.getUsername()) && this.getId() == that.getId();
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), salary);
    }

    @Override
    public String toString() {
        return "RegularStaff{" +
                super.toString() +
                ", salary=" + salary +
                "$}";
    }
}
