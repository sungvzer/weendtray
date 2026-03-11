package it.salvatoregargano.weendtray.acl;

import it.salvatoregargano.weendtray.telephone.billing.PhonePlan;

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

    public RegularUser(String username, String password, String name, String surname, PhonePlan plan,
            String phoneNumber, boolean active) {
        this(-1, username, password, name, surname, plan, phoneNumber, active);
    }

    public RegularUser(int id, String username, String password, String name, String surname, PhonePlan plan,
            String phoneNumber, boolean active) {
        super(id, username, password, name, surname, UserRole.USER, active);
        phonePlan = plan;
        this.phoneNumber = phoneNumber;
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
}
