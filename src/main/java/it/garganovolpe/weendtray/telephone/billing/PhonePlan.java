package it.garganovolpe.weendtray.telephone.billing;

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

    public int getMinutesLimit() {
        switch (name) {
            case "REGULAR":
                return 100;
            case "PREMIUM":
                return 500;
            case "BUSINESS":
                return 1000;
            default:
                throw new IllegalArgumentException("Unknown phone plan: " + name);
        }
    }

    public int getMessagesLimit() {
        switch (name) {
            case "REGULAR":
                return 100;
            case "PREMIUM":
                return 500;
            case "BUSINESS":
                return 1000;
            default:
                throw new IllegalArgumentException("Unknown phone plan: " + name);
        }
    }

    public double getDataLimitMB() {
        switch (name) {
            case "REGULAR":
                return 512;
            case "PREMIUM":
                return 2048;
            case "BUSINESS":
                return 5120;
            default:
                throw new IllegalArgumentException("Unknown phone plan: " + name);
        }
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

    public double getRenewalCost() {
        switch (name) {
            case "REGULAR":
                return 10.0;
            case "PREMIUM":
                return 30.0;
            case "BUSINESS":
                return 50.0;
            default:
                throw new IllegalArgumentException("Unknown phone plan: " + name);
        }
    }
}
