package it.garganovolpe.weendtray.acl;

public interface Builder {
    Builder withUsername(String username);

    Builder withId(int id);

    Builder withName(String name);

    Builder withSurname(String surname);

    Builder withPlainTextPassword(String plainTextPassword);

    Builder withHashedPassword(String hashedPassword);

    Builder withRole(UserRole role);

    Builder withActive(boolean active);

    User build();
}
