package it.salvatoregargano.weendtray.acl;

import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;
import it.salvatoregargano.weendtray.telephone.billing.UserAccountKind;

public class UserTypeDirector {
    public static User buildAdminUser(String username, int id, String plainTextPassword, String name, String surname) {
        return new UserBuilder()
                .withId(id)
                .withUsername(username)
                .withPlainTextPassword(plainTextPassword)
                .withName(name)
                .withSurname(surname)
                .withRole(UserRole.ADMIN)
                .build();
    }

    public static RegularUser buildRegularUser(String username, int id, String plainTextPassword, String name, String surname, String phoneNumber, PhonePlan phonePlan, UserAddress address, UserAccountKind kind) {
        return new RegularUserBuilder()
                .withId(id)
                .withUsername(username)
                .withPlainTextPassword(plainTextPassword)
                .withName(name)
                .withSurname(surname)
                .withPhoneNumber(phoneNumber)
                .withPhonePlan(phonePlan)
                .withRole(UserRole.USER)
                .withAddress(address)
                .withKind(kind)
                .build();
    }
}