package it.garganovolpe.weendtray.telephone;

import java.time.Duration;

import it.garganovolpe.weendtray.telephone.billing.PhonePlan;

// Call Event
public class CallEvent extends PhoneEvent {
    private final Duration duration;

    public CallEvent(PhonePlan plan, String sourceNumber, String targetNumber, Duration duration) {
        super(sourceNumber, targetNumber, plan);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String getDescription() {
        return "[" + getUUID() + "][CALL] from " + getSourceNumber() + " to " + getTargetNumber() + " ("
                + duration.toMinutes() + " minutes)";
    }
}
