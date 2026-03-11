package it.salvatoregargano.weendtray.telephone.billing;

/**
 * The PhonePlan class represents a phone plan that users can subscribe to. Each
 * phone plan has a name and an associated billing strategy that defines how
 * costs
 * are calculated for different types of phone events, such as calls, messages,
 * and data usage.
 */
public class PhonePlan {
    public static final PhonePlan PREMIUM = new PhonePlan("PREMIUM", new PremiumBillingStrategy());
    public static final PhonePlan REGULAR = new PhonePlan("REGULAR", new RegularBillingStrategy());
    public static final PhonePlan BUSINESS = new PhonePlan("BUSINESS", new BusinessBillingStrategy());

    private final String name;
    private final BillingStrategy billingStrategy;

    public PhonePlan(String name, BillingStrategy billingStrategy) {
        this.name = name;
        this.billingStrategy = billingStrategy;
    }

    public BillingStrategy getBillingStrategy() {
        return billingStrategy;
    }

    @Override
    public String toString() {
        return name;
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
