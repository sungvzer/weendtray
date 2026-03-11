package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;

public class RegularBillingStrategy implements BillingStrategy {
    @Override
    public double calculateCallCost(CallEvent event) {
        return 0.1 * event.getDuration().toMinutes();
    }

    @Override
    public double calculateMessageCost(MessageEvent event) {
        return 0.01 * event.getContent().length();
    }

    @Override
    public double calculateDataCost(DataUsageEvent event) {
        return 0.005 * event.getDataSizeKB();
    }
}
