
public interface IStaff {

    // Getter Methods
    int getId();
    String getName();
    String getRole();
    double getSalary();
    String getUsername();
    boolean getStatus();

    // Setter Methods
    void setName(String name);
    void setRole(String role);
    void setSalary(double salary);
    void setUsername(String username);
    void setPassword(String password);
    void setStatus(boolean status);

    boolean can(String action);

}