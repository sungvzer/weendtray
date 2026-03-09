package it.salvatoregargano.weendtray.acl;

public class PhonePlan {
    public static final PhonePlan PREMIUM = new PhonePlan("PREMIUM");
    public static final PhonePlan REGULAR = new PhonePlan("REGULAR");
    public static final PhonePlan BUSINESS = new PhonePlan("BUSINESS");

    public final String name;

    public PhonePlan(String name) {
        this.name = name;
    }

    public static PhonePlan[] values() {
        return new PhonePlan[] { REGULAR, PREMIUM, BUSINESS };
    }

    public static PhonePlan valueOf(String name) {
        switch (name) {
            case "REGULAR":
                return REGULAR;
            case "PREMIUM":
                return PREMIUM;
            case "BUSINESS":
                return BUSINESS;
            default:
                throw new IllegalArgumentException("Unknown phone plan: " + name);
        }
    }
}
