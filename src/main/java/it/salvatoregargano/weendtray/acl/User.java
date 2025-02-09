package it.salvatoregargano.weendtray.acl;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class User {
    private final String username;
    private final String password;
    private final String name;
    private final String surname;
    private UserRole role;
    private int id;

    public User(String username, String password, String name, String surname, UserRole role) {
        this(-1, username, password, name, surname, role);
    }

    public User(int id, String username, String password, String name, String surname, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public static String hashPassword(String password) {
        // hash the password with bcrypt
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verifyPassword(String password) {
        // verify the password with bcrypt
        return BCrypt.verifyer().verify(password.toCharArray(), this.password).verified;
    }

    public UserRole getRole() {
        return role;
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
