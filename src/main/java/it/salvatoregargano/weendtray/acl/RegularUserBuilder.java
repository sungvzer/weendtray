package it.salvatoregargano.weendtray.acl;

import java.util.Optional;

import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;

/**
 * Helper class for building {@link User} objects.
 */
public class UserBuilder implements Builder {
    private String username;
    private String hashedPassword;
    private UserRole role;
    private String name;
    private String surname;
    private String phoneNumber;
    private PhonePlan phonePlan;
    private Optional<Integer> id;
    private Optional<UserAddress> address;
    private Optional<UserAccountKind> kind;
    private boolean active = true;

    public UserBuilder() {
        id = Optional.empty();
        address = Optional.empty();
        kind = Optional.empty();
    }

    @Override
    public UserBuilder withId(int id) {
        this.id = Optional.of(id);
        return this;
    }

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

    public UserBuilder withAddress(UserAddress address) {
        this.address = Optional.of(address);
        return this;
    }

    public UserBuilder withKind(UserAccountKind kind) {
        this.kind = Optional.of(kind);
        return this;
    }

    public UserBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public User build() {
        if (role == UserRole.USER) {
            var regularUser = new RegularUser(username, hashedPassword, name, surname, phonePlan, phoneNumber, active,
                    address.orElse(null), kind.orElse(null));
            regularUser.setId(id.orElse(-1));
            return regularUser;
        } else {
            var user = new User(username, hashedPassword, name, surname, role, active);
            user.setId(id.orElse(-1));
            return user;
        }
    }
}
