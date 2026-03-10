package user;

public interface IStaff {

    // Getter Methods
    int getId();
    String getName();
    String getUsername();
    boolean getStatus();

    // Setter Methods
    void setName(String name);
    void setUsername(String username);
    void setPassword(String password);
    void setStatus(boolean status);
    void setActive(boolean active);

    // Other Methods
    boolean isActive();
    boolean checkPassword(String input);
    boolean can(String action);

}