package user;

public class ManagerStaff extends Staff {

    /*====== Register ====== */
    public ManagerStaff(String name, String role, double salary, String username, String password) {
        super(name, role, salary, username, password); // parent (user.Staff) runs first
    }

    /*====== Manager Permissions — full access ====== */
    @Override
    public boolean can(String action) {
        return true;
    }
}