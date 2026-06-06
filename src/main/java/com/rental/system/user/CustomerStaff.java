package com.rental.system.user;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerStaff extends Staff {

    public CustomerStaff() {
        super();
        this.setStatus(true);
    }

    public CustomerStaff(String name, String username, String password) {
        super(name, username, password, 0.0);
        this.setStatus(true);
    }

    @Override
    public boolean can(String action) {
        return "VIEW_VEHICLE".equals(action) || "RESERVE_VEHICLE".equals(action);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomerStaff that = (CustomerStaff) o;
        return this.getUsername().equals(that.getUsername()) && this.getId() == that.getId();
    }

    @Override
    public String toString() {
        return "CustomerStaff{" + super.toString() + "}";
    }
}
