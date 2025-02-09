package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.acl.PhonePlan;

// Data Usage Event
public class DataUsageEvent extends PhoneEvent {
    private final int dataSizeMB;

    public DataUsageEvent(PhonePlan plan, String sourceNumber, int dataSizeMB) {
        super(sourceNumber, "Internet", plan); // No target number for data usage
        this.dataSizeMB = dataSizeMB;
    }

    public int getDataSizeMB() {
        return dataSizeMB;
    }

    @Override
    public String getDescription() {
        return "Used " + dataSizeMB + "MB of mobile data";
    }
}
