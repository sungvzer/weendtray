package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;

public class PremiumBillingStrategy implements BillingStrategy {
    @Override
    public double calculateMessageCost(MessageEvent event) {
        return 0.0075 * event.getContent().length();
    }

    @Override
    public double calculateCallCost(CallEvent event) {
        return 0.05 * event.getDuration().toMinutes();
    }

    @Override
    public double calculateDataCost(DataUsageEvent event) {
        return 0.00025 * event.getDataSizeKB();
    }
}
