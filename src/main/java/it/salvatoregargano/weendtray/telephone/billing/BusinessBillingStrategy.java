package it.salvatoregargano.weendtray.telephone.billing;

import it.salvatoregargano.weendtray.telephone.CallEvent;
import it.salvatoregargano.weendtray.telephone.DataUsageEvent;
import it.salvatoregargano.weendtray.telephone.MessageEvent;

public class BusinessBillingStrategy implements BillingStrategy {
    @Override
    public double calculateMessageCost(MessageEvent event) {
        return 0;
    }

    @Override
    public double calculateCallCost(CallEvent event) {
        return 0;
    }

    @Override
    public double calculateDataCost(DataUsageEvent event) {
        return 0;
    }
}
