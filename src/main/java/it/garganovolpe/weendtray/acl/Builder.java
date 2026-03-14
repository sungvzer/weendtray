package it.garganovolpe.weendtray.acl;
/*
 * A builder for creating user instances.
 * It provides a fluent interface for setting {@link User} properties and building the final {@link User} object.
*/
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
