package user;

import controller.Garage;

import java.util.Objects;

public class RegularStaff extends Staff {

    private String workStation;
    /*====== Register ====== */
    public RegularStaff(String name,  String username, String password, double salary, String workStation) {
        super(name, username, password, salary); // parent (user.Staff) runs first
        this.setWorkStation(workStation);
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

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegularStaff that = (RegularStaff) o;
        return this.getUsername().equals(that.getUsername()) && this.getId() == that.getId();
    }


    @Override
    public String toString() {
        return "RegularStaff{" +
                super.toString() +
                "workStation='" + workStation + '\'' +
                '}';
    }
}
