package user;

import controller.Garage;

public class ManagerStaff extends Staff {

    private double bonus;

    /*====== Register ====== */
    public ManagerStaff(String name, String username, String password, double salary) {
        super(name, username, password, salary); // parent (user.Staff) runs first
        this.bonus = 0;
    }

    /*====== Manager Permissions — full access ====== */
    @Override
    public boolean can(String action) {
        return !action.equals(Garage.SET_MANAGER_SALARY);
    }

    public double getBonus() {return bonus;}

    public void setBonus(double bonus) {
        this.bonus = bonus>0 ? bonus : 0;
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
                ", bonus=" + bonus +
                "$}";
    }
}