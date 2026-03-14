package it.garganovolpe.weendtray.acl;

import it.garganovolpe.weendtray.telephone.billing.PhonePlan;
import it.garganovolpe.weendtray.telephone.billing.UserAccountKind;

/**
 * The RegularUser class represents a standard user of the application,
 * extending the base User class. It includes additional attributes specific to
 * regular users, such as their phone plan and phone number. This class provides
 * constructors for creating regular user instances and getter/setter methods
 * for accessing and modifying the phone plan and phone number. Regular users
 * are typically the primary users of the application, and this class
 * encapsulates the relevant information and behaviors associated with them.
 */
public class RegularUser extends User {
    private PhonePlan phonePlan;
    private String phoneNumber;
    private UserAddress address;
    private UserAccountKind kind;

    public RegularUser(String username, String password, String name, String surname, PhonePlan plan,
            String phoneNumber, boolean active, UserAddress address, UserAccountKind kind) {
        this(-1, username, password, name, surname, plan, phoneNumber, active, address, kind);
    }

    public RegularUser(int id, String username, String password, String name, String surname, PhonePlan plan,
            String phoneNumber, boolean active, UserAddress address, UserAccountKind kind) {
        super(id, username, password, name, surname, UserRole.USER, active);
        phonePlan = plan;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.kind = kind;
    }

    public PhonePlan getPhonePlan() {
        return phonePlan;
    }

    public void setPhonePlan(PhonePlan phonePlan) {
        this.phonePlan = phonePlan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public UserAccountKind getKind() {
        return kind;
    }

    public void setKind(UserAccountKind kind) {
        this.kind = kind;
    }

}
