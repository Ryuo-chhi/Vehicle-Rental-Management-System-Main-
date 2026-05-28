package com.rental.system.user;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("REGULAR")
public class RegularStaff extends Staff {

    @Column(name = "work_station")
    private String workStation;

    public RegularStaff() {
        super();
    }

    /*====== Register ====== */
    public RegularStaff(String name,  String username, String password, double salary, String workStation) {
        super(name, username, password, salary); // parent (com.rental.system.user.Staff) runs first
        this.setWorkStation(workStation);
    }


    /*====== Regular com.rental.system.user.Staff Permissions ====== */
    @Override
    public boolean can(String action) {
        return "VIEW_VEHICLE".equals(action)    ||
               "VIEW_CUSTOMER".equals(action)   ||
               "MANAGE_CUSTOMER".equals(action) ||
               "ADD_RENT".equals(action)        ||
               "VIEW_RENT".equals(action)       ||
               "RETURN_VEHICLE".equals(action)  ||
               "SHOW_PAYMENT".equals(action);
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
                ", workStation='" + workStation + '\'' +
                '}';
    }
}
