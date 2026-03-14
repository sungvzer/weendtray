package it.garganovolpe.weendtray.telephone;

import java.util.UUID;

import it.garganovolpe.weendtray.telephone.billing.PhonePlan;
/*
    * An abstract class representing a generic phone event, such as a call, message, or data usage.
    * This class encapsulates common properties of phone events, including the source and target phone numbers,
    * the associated phone plan, and a unique identifier for the event.
    * @see CallEvent
    * @see MessageEvent
    * @see DataUsageEvent
*/
public abstract class PhoneEvent {
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
