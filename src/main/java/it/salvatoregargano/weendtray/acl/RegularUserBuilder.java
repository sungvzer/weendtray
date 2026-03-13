package it.salvatoregargano.weendtray.acl;

import java.util.Optional;

import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;

/**
 * Helper class for building {@link User} objects.
 */
public class RegularUserBuilder implements Builder {
    private String username;
    private String hashedPassword;
    private String name;
    private String surname;
    private String phoneNumber;
    private UserRole role;
    private PhonePlan phonePlan;
    private Optional<Integer> id;
    private Optional<UserAddress> address;
    private Optional<UserAccountKind> kind;
    private boolean active = true;

    public RegularUserBuilder() {
        id = Optional.empty();
        address = Optional.empty();
        kind = Optional.empty();
    }

    @Override
    public RegularUserBuilder withId(int id) {
        this.id = Optional.of(id);
        return this;
    }

    @Override
    public RegularUserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RegularUserBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public RegularUserBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RegularUserBuilder withPhonePlan(PhonePlan phonePlan) {
        this.phonePlan = phonePlan;
        return this;
    }

    public RegularUserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public RegularUserBuilder withPlainTextPassword(String plainTextPassword) {
        hashedPassword = User.hashPassword(plainTextPassword);
        return this;
    }

    @Override
    public RegularUserBuilder withHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    @Override
    public RegularUserBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public RegularUserBuilder withAddress(UserAddress address) {
        this.address = Optional.of(address);
        return this;
    }

    public RegularUserBuilder withKind(UserAccountKind kind) {
        this.kind = Optional.of(kind);
        return this;
    }

    @Override
    public RegularUserBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public RegularUser build() {        
        var regularUser = new RegularUser(username, hashedPassword, name, surname, phonePlan, phoneNumber, active,
            address.orElse(null), kind.orElse(null));
        regularUser.setId(id.orElse(-1));
        return regularUser;
        
    }
}
