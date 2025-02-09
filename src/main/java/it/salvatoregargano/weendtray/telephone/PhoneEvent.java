package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.acl.PhonePlan;

import java.util.UUID;

// Base class for events (abstract to enforce subclassing)
abstract class PhoneEvent {
    private final String sourceNumber;
    private final String targetNumber;
    private final PhonePlan plan;
    private final UUID uuid;

    public PhoneEvent(String sourceNumber, String targetNumber, PhonePlan plan) {
        this.targetNumber = targetNumber;
        this.sourceNumber = sourceNumber;
        this.plan = plan;
        this.uuid = UUID.randomUUID();
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public abstract String getDescription();

    public String getSourceNumber() {
        return sourceNumber;
    }

    public PhonePlan getPlan() {
        return plan;
    }

    public UUID getUUID() {
        return uuid;
    }
}

