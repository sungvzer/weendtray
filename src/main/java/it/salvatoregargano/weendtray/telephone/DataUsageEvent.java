package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.acl.PhonePlan;

// Data Usage Event
public class DataUsageEvent extends PhoneEvent {
    private final int dataSizeKB;

    public DataUsageEvent(PhonePlan plan, String sourceNumber, int dataSizeBytes) {
        super(sourceNumber, "Internet", plan); // No target number for data usage
        this.dataSizeKB = dataSizeBytes / 1024;
    }

    public int getDataSizeKB() {
        return dataSizeKB;
    }

    @Override
    public String getDescription() {
        return "[" + getUUID() + "][DATA] " + getSourceNumber() + " used " + getDataSizeKB() + " KB";
    }
}
