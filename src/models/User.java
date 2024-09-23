package models;

public class User {
    public enum Role {
        SELLER, BUYER
    }

    private String username;
    private String password;
    private String email;
    private Role role;

    // Constructor
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
