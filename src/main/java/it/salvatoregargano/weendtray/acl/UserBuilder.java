package it.salvatoregargano.weendtray.acl;

import java.util.Optional;

/**
 * Helper class for building {@link User} objects.
 */
public class UserBuilder implements Builder {
    private String username;
    private String hashedPassword;
    private String name;
    private String surname;
    private UserRole role;
    private Optional<Integer> id;
    private boolean active = true;

    public UserBuilder() {
        id = Optional.empty();
    }

    @Override
    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public UserBuilder withId(int id) {
        this.id = Optional.of(id);
        return this;
    }

    @Override
    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public UserBuilder withPlainTextPassword(String plainTextPassword) {
        hashedPassword = User.hashPassword(plainTextPassword);
        return this;
    }

    @Override
    public UserBuilder withHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    @Override
    public UserBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    @Override
    public UserBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public User build() {
        var adminUser = new User(username, hashedPassword, name, surname, role, active);
        adminUser.setId(id.orElse(-1));
        return adminUser;
    }
}
