package it.garganovolpe.weendtray.acl;

/**
 * The UserRole enum represents the different roles that a user can have in the
 * system. Each role defines a set of permissions and access levels for the
 * user. The two
 * roles defined in this enum are:
 * <ul>
 * <li>{@link UserRole.ADMIN}: This role has full access to all features and
 * functionalities of
 * the system. Users with the ADMIN role can manage other users, view and modify
 * all data, and perform any administrative tasks.</li>
 * <li>{@link UserRole.USER}: This role has limited access to the system. Users
 * with the USER
 * role can access their own data and perform actions that are allowed for
 * regular users, but they cannot manage other users or access administrative
 * features.</li>
 * </ul>
 */
public enum UserRole {
    ADMIN,
    USER
}
