package it.salvatoregargano.weendtray.acl;

public class UserBuilder {
    private String username;
    private String hashedPassword;
    private UserRole role;
    private String name;
    private String surname;
    private String phoneNumber;
    private PhonePlan phonePlan;

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserBuilder withPhonePlan(PhonePlan phonePlan) {
        this.phonePlan = phonePlan;
        return this;
    }

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
        if (role == UserRole.USER) {
            var regularUser = new RegularUser(username, hashedPassword, name, surname, phonePlan, phoneNumber, true);
            return regularUser;
        } else {
            var user = new User(username, hashedPassword, name, surname, role, true);
            return user;
        }
    }
}
