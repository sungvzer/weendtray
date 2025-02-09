package it.salvatoregargano.weendtray.acl;

public class User {
    private final String username;
    private final String password;
    private final String email;
    private final String name;
    private final String surname;
    private final UserRole role;
    private int id;

    public User(String username, String password, String email, String name, String surname, UserRole role) {
        this(-1, username, password, email, name, surname, role);
    }


    public User(int id, String username, String password, String email, String name, String surname, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
