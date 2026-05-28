package com.rental.system.user;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MANAGER")
public class ManagerStaff extends Staff {

    @Column(name = "bonus")
    private double bonus;

    public ManagerStaff() {
        super();
    }

    /*====== Register ====== */
    public ManagerStaff(String name, String username, String password, double salary) {
        super(name, username, password, salary); // parent (com.rental.system.user.Staff) runs first
        this.bonus = 0;
    }

    /*====== Manager Permissions — full access ====== */
    @Override
    public boolean can(String action) {
        if ("admin_root".equalsIgnoreCase(getUsername())) {
            return true;
        }
        return !action.equals("SET_MANAGER_SALARY");
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