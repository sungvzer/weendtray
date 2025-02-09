package it.salvatoregargano.weendtray.acl;

public class RegularUser extends User {
    private PhonePlan phonePlan;
    private String phoneNumber;

    public RegularUser(String username, String password, String name, String surname, PhonePlan plan, String phoneNumber) {
        this(-1, username, password, name, surname, plan, phoneNumber);
    }

    public RegularUser(int id, String username, String password, String name, String surname, PhonePlan plan, String phoneNumber) {
        super(id, username, password, name, surname, UserRole.USER);
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
