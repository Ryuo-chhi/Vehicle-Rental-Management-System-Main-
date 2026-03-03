package user;

import controller.Garage;

public class RegularStaff extends Staff {

    /*====== Register ====== */
    public RegularStaff(String name, String role, double salary, String username, String password) {
        super(name, role, salary, username, password); // parent (user.Staff) runs first
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
    @Override
    public void setSalary (double salary){
        System.out.println("Error: user.RegularStaff can not modify salary");
        return;
    }
}
