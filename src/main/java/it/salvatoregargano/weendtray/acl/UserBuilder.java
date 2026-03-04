package it.salvatoregargano.weendtray.acl;

public class UserBuilder {
    private String username;
    private String hashedPassword;
    private UserRole role;

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withPlainTextPassword(String plainTextPassword) {
        hashedPassword = User.hashPassword(plainTextPassword);
        return this;
    }

    public UserBuilder withHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public UserBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public User build() {
        return new User(username, hashedPassword, "", "", role, true);
    }
}
