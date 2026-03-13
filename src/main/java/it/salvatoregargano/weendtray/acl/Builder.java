package it.salvatoregargano.weendtray.acl;

public interface Builder {
    Builder withId(int id);
    Builder withName(String name);
    Builder withSurname(String surname);
    Builder withPlainTextPassword(String plainTextPassword);
    Builder withHashedPassword(String hashedPassword);
    Builder withRole(UserRole role);
    Builder withActive(boolean active);
    User build();
}
