package it.salvatoregargano.weendtray.acl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class User {
    private String username;
    private String password;
    private String name;

    private String surname;
    private UserRole role;
    private boolean active;
    private int id;

    BooleanProperty isAdmin;

    public BooleanProperty isAdminProperty() {
        return isAdmin;
    }

    public User(String username, String password, String name, String surname, UserRole role, boolean active) {
        this(-1, username, password, name, surname, role, active);
    }

    public User(int id, String username, String password, String name, String surname, UserRole role, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.active = active;
        this.isAdmin = new SimpleBooleanProperty(role == UserRole.ADMIN);
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

    public boolean isActive() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setRole(UserRole role) {
        this.role = role;
        this.isAdmin.set(role == UserRole.ADMIN);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
